package org.hsweb.expands.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 脚本执行器
 * Created by zhouhao on 16-6-28.
 */
public class Shell {
    //默认字符集
    private static final String DEFAULT_ENCODE;

    private String encode;
    
    private List<String> env;

    private List<String> commands;

    private List<Callback> processCallback = new LinkedList<>();

    private List<Callback> errorCallback = new LinkedList<>();

    private boolean shutdown = false;

    private File dir;

    private ProcessHelper helper;

    static {
        String os = System.getProperty("os.name");
        if ("windows".equals(os.toLowerCase())) {
            DEFAULT_ENCODE = "gbk";
        } else {
            DEFAULT_ENCODE = "utf-8";
        }
    }

    public Shell(String command, String... more) {
        encode = DEFAULT_ENCODE;
        commands = new ArrayList<>(Arrays.asList(command));
        commands.addAll(Arrays.asList(more));
        dir = new File("./");
        helper = () -> shutdown = true;
    }

    public static Shell build(String command, String... more) {
        return new Shell(command, more);
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

    public int exec() throws IOException {
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

    private int process(final Process process, String encode) {
        try {
            BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream(), encode));
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
            String line;
            while ((line = output.readLine()) != null) {
                if (shutdown) process.destroyForcibly();
                String tmp = line;
                processCallback.forEach(consumer -> consumer.accept(tmp, helper));
            }
            return process.waitFor();
        } catch (Exception e) {
            errorCallback.forEach(consumer -> consumer.accept(e.getMessage(), helper));
        }
        return -1;
    }

}
