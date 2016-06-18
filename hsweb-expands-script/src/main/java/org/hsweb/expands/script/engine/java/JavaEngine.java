package org.hsweb.expands.script.engine.java;

import org.hsweb.commons.ClassUtils;
import org.hsweb.commons.StringUtils;
import org.hsweb.commons.file.FileUtils;
import org.hsweb.expands.script.engine.DynamicScriptEngine;
import org.hsweb.expands.script.engine.ExecuteResult;
import org.hsweb.expands.script.engine.common.listener.CommonScriptExecuteListener;
import org.hsweb.expands.script.engine.listener.ExecuteEvent;
import org.hsweb.expands.script.engine.listener.ScriptExecuteListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;
import javax.tools.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 浩 on 2015-10-27 0027.
 */
public class JavaEngine implements DynamicScriptEngine {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private String savePath = null;
    private String classpath = "";
    private Map<String, Class> cache = new ConcurrentHashMap<>();
    public static Map<String, Executor> executorCache = new ConcurrentHashMap<>();
    protected Map<String, CommonScriptExecuteListener> listenerMap = new HashMap<>();

    public JavaEngine() {
        savePath = System.getProperty("java.io.tmpdir").concat("/org/hsweb/java/engine/");
        new File(savePath + "src").mkdirs();
        new File(savePath + "bin").mkdirs();
        classpath = System.getProperty("java.class.path");
    }

    @Override
    public void init(String... contents) throws Exception {

    }

    public static void main(String[] args) {
        String name = StringUtils.matcherFirst("class\\s+([\\w\\d$_]+)s*", "public class Test{}//");
        System.out.println(name);
    }

    public String getClassName(String code) {
        String name = StringUtils.matcherFirst("class\\s+([\\w\\d$_]+)s*", code);
        if (name == null) return name;
        return name.substring(5, name.length()).trim();
    }

    public String getPackage(String code) {
        String name = StringUtils.matcherFirst("package\\s+([\\w\\d$.]+)s*", code);
        if (name == null) return name;
        return name.substring(7, name.length()).trim();
    }

    @Override
    public boolean compile(String id, String code) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        List<JavaFileObject> jfiles = new ArrayList<>();
        String name = getClassName(code);
        String packageName = getPackage(code);
        if (!StringUtils.isNullOrEmpty(packageName)) {
            name = packageName + "." + name;
        }
        String fileName = savePath + "src/" + name.replace('.', '/') + ".java";
        File file = new File(fileName);
        if (file.exists()) file.delete();
        FileUtils.writeString2File(code, fileName, "utf-8");
        StandardJavaFileManager fm = compiler.getStandardFileManager(null, null, null);
        jfiles.add(new CharSequenceJavaFileObject(savePath, name, code));
        List<String> options = new ArrayList<String>();
        options.add("-d");
        options.add(savePath + "bin");
        options.add("-encoding");
        options.add("UTF-8");
        options.add("-classpath");
        options.add(classpath);
        if (logger.isDebugEnabled()) {
            logger.debug("javac [{}] -> {}",fileName, options.stream().reduce((s, s2) -> s + " " + s2).get());
            logger.debug(code);
        }
        JavaCompiler.CompilationTask task = compiler.getTask(null, fm, diagnostics, options, null, jfiles);
        boolean success = task.call();
        if (success) {
            DynamicClassLoader dynamicClassLoader = new DynamicClassLoader(new URL[]{
                    new File(savePath + "bin").toURI().toURL()
            }, JavaEngine.class.getClassLoader());
            Class<?> clazz = dynamicClassLoader.loadClass(name);
            cache.put(id, clazz);
            executorCache.remove(id);
            if (ClassUtils.instanceOf(clazz, Executor.class)) {
                executorCache.put(id, (Executor) clazz.newInstance());
            }
            return clazz != null;
        } else {
            StringBuilder builder = new StringBuilder();
            for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                builder.append(diagnostic).append("\n");
            }
            throw new ScriptException(builder.toString());
        }
    }

    @Override
    public ExecuteResult execute(String id) {
        return execute(id, new HashMap<String, Object>());
    }

    @Override
    public ExecuteResult execute(String id, Map<String, Object> param) {
        long startTime = System.currentTimeMillis();
        ExecuteResult result = new ExecuteResult();
        try {
            Executor executor = executorCache.get(id);
            if (executor != null) {
                result.setResult(executor.execute(param));
                result.setSuccess(true);
            } else {
                Class clazz = cache.get(id);
                if (clazz == null) {
                    result.setSuccess(false);
                    result.setResult(null);
                    result.setMessage(String.format("class(%s): %s not found!", id, "java"));
                } else {
                    result.setSuccess(false);
                    result.setResult(clazz);
                    result.setMessage(String.format("class(%s): %s found! but not a executor;", id, "java"));
                }
            }
        } catch (Exception e) {
            result.setException(e);
        }
        result.setUseTime(System.currentTimeMillis() - startTime);
        if (listenerMap.size() > 0)
            for (CommonScriptExecuteListener listener : listenerMap.values()) {
                listener.onExecute(new ExecuteEvent(ExecuteEvent.TYPE_EXECUTE, id, result));
            }
        return result;
    }

    @Override
    public <T extends ScriptExecuteListener> T addListener(T listener) throws Exception {
        if (listener instanceof CommonScriptExecuteListener)
            listenerMap.put(listener.getName(), (CommonScriptExecuteListener) listener);
        return listener;
    }

    @Override
    public void removeListener(String name) throws Exception {
        listenerMap.remove(name);
    }

    @Override
    public boolean compiled(String id) {
        return cache.get(id) != null;
    }


}
