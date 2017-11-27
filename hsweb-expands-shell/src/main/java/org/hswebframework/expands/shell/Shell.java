package org.hswebframework.expands.shell;

import org.hswebframework.expands.shell.build.LinuxShellBuilder;
import org.hswebframework.expands.shell.build.WindowsShellBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 脚本执行器
 * Created by zhouhao on 16-6-28.
 */
public class Shell {
    //默认字符集
    private static final String DEFAULT_ENCODE;

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    private String encode;

    private List<String> env;

    private List<String> commands;

    private List<Callback> processCallback = new LinkedList<>();

    private List<Callback> errorCallback = new LinkedList<>();

    private List<Consumer<ProcessHelper>> execBeforeCallback = new LinkedList<>();

    private static ShellBuilder shellBuilder;

    private boolean shutdown = false;

    private File dir;

    private ProcessHelper helper;

    private OutputStream stdin;

    static {
        String os = System.getProperty("os.name");
        if ("windows".equals(os.toLowerCase())) {
            DEFAULT_ENCODE = "gbk";
            shellBuilder = new WindowsShellBuilder();
        } else {
            DEFAULT_ENCODE = "utf-8";
            shellBuilder = new LinuxShellBuilder();
        }
    }

    public Shell(String command, String... more) {
        encode = DEFAULT_ENCODE;
        commands = new ArrayList<>(Arrays.asList(command));
        commands.addAll(Arrays.asList(more));
        dir = new File("./");
        helper = new ProcessHelper() {
            @Override
            public void kill() {
                shutdown = true;
            }

            @Override
            public void sendMessage(byte[] msg) throws IOException {
                if (null != stdin) {
                    stdin.write(msg);
                    stdin.flush();
                }
            }
        };
    }

    public static Shell build(String command, String... more) {
        return new Shell(command, more);
    }

    public static Shell buildText(String text) throws Exception {
        return shellBuilder.buildTextShell(text);
    }

    public Shell dir(File file) {
        dir = file;
        return this;
    }

    public Shell dir(String dir) {
        return dir(new File(dir));
    }

    public Shell encode(String encode) {
        this.encode = encode;
        return this;
    }

    public Shell env(String... env) {
        if (this.env == null) {
            this.env = Arrays.asList(env);
        } else {
            this.env.addAll(Arrays.asList(env));
        }
        return this;
    }

    public Shell onProcess(Callback callback) {
        processCallback.add(callback);
        return this;
    }

    public Shell onError(Callback callback) {
        errorCallback.add(callback);
        return this;
    }

    public Shell before(Consumer<ProcessHelper> consumer) {
        execBeforeCallback.add(consumer);
        return this;
    }

    public Result exec() throws IOException {
        if (this.commands.size() > 1) {
            String[] envp;
            if (this.env == null || this.env.isEmpty()) envp = new String[0];
            else envp = this.env.toArray(new String[this.env.size()]);
            Process process = Runtime.getRuntime()
                    .exec(this.commands.toArray(new String[this.commands.size()])
                            , envp, dir);
            return process(process, encode);
        } else {
            Process process = Runtime.getRuntime()
                    .exec(this.commands.get(0));
            return process(process, encode);
        }
    }

    public Future<Result> execAsyn() {
        return executorService.submit(this::exec);
    }

    public void execAsyn(Consumer<Result> consumer) {
        executorService.execute(() -> {
            try {
                consumer.accept(exec());
            } catch (IOException e) {
                consumer.accept(new Result(-1, e, e.getMessage()));
            }
        });
    }

    private Result process(final Process process, String encode) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), encode));
            stdin = process.getOutputStream();
            new Thread(() -> {
                try {
                    BufferedReader errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream(), encode));
                    String line;
                    while ((line = errorStream.readLine()) != null) {
                        if (shutdown) process.destroyForcibly();
                        String tmp = line;
                        errorCallback.forEach(consumer -> consumer.accept(tmp, helper));
                    }
                } catch (final Exception e) {
                    errorCallback.forEach(consumer -> consumer.accept(e.getMessage(), helper));
                }
            }).start();
            execBeforeCallback.forEach(consumer -> consumer.accept(helper));
            String line;
            while ((line = reader.readLine()) != null) {
                if (shutdown) process.destroyForcibly();
                String tmp = line;
                processCallback.forEach(consumer -> consumer.accept(tmp, helper));
            }

            return new Result(process.waitFor(), null, null);
        } catch (Exception e) {
            errorCallback.forEach(consumer -> consumer.accept(e.getMessage(), helper));
            return new Result(-1, e, e.getMessage());
        } finally {
            stdin = null;
        }
    }

}
