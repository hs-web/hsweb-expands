package org.hsweb.expands.script.engine.java;

import org.hsweb.commons.ClassUtils;
import org.hsweb.commons.MD5;
import org.hsweb.commons.StringUtils;
import org.hsweb.commons.file.FileUtils;
import org.hsweb.expands.script.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.ClassLoaderUtil;

import javax.script.ScriptException;
import javax.tools.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 浩 on 2015-10-27 0027.
 */
public class JavaEngine extends ListenerSupportEngine {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private String savePath = null;
    private String classpath = "";
    private Map<String, JavaCodeContext> cache = new ConcurrentHashMap<>();

    private URL[] loaderUrl;

    public JavaEngine() throws Exception {
        savePath = System.getProperty("java.io.tmpdir").concat("/org/hsweb/java/engine/");
        new File(savePath + "src").mkdirs();
        new File(savePath + "bin").mkdirs();
        classpath = System.getProperty("java.class.path");
        loaderUrl = new URL[]{new File(savePath + "bin").toURI().toURL()};
    }

    @Override
    public void init(String... contents) throws Exception {

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
        String name = getClassName(code);
        String packageName = getPackage(code);
        if (!StringUtils.isNullOrEmpty(packageName)) {
            name = packageName + "." + name;
        }
        String fileName = savePath + "src/" + name.replace('.', '/') + ".java";
        File file = new File(fileName);
        if (file.exists()) file.delete();
        FileUtils.writeString2File(code, fileName, "utf-8");

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        List<JavaFileObject> jfiles = new ArrayList<>();
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
            logger.debug("javac [{}] -> {}", fileName, options.stream().reduce((s, s2) -> s + " " + s2).get());
            logger.debug(code);
        }
        JavaCompiler.CompilationTask task = compiler.getTask(null, fm, diagnostics, options, null, jfiles);
        boolean success = task.call();
        if (success) {
            DynamicClassLoader dynamicClassLoader = new DynamicClassLoader(loaderUrl, JavaEngine.class.getClassLoader());
            Class<?> clazz = dynamicClassLoader.loadClass(name);
            Executor executor = null;
            if (ClassUtils.instanceOf(clazz, Executor.class)) {
                executor = (Executor) clazz.newInstance();
            }
            JavaCodeContext context = new JavaCodeContext(id, MD5.defaultEncode(code), clazz, executor);
            cache.put(id, context);
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
        return execute(id, new HashMap<>());
    }

    @Override
    public ExecuteResult execute(String id, Map<String, Object> param) {
        long startTime = System.currentTimeMillis();
        ExecuteResult result = new ExecuteResult();
        JavaCodeContext context = cache.get(id);
        try {
            if (context != null) {
                doListenerBefore(context);
                Executor executor = context.getExecutor();
                Class clazz = context.getCodeClass();
                if (executor != null) {
                    result.setResult(executor.execute(param));
                    result.setSuccess(true);
                } else {
                    result.setSuccess(true);
                    result.setResult(clazz);
                }
            } else {
                result.setSuccess(false);
                result.setResult(null);
                result.setMessage(String.format("class(%s): %s not found!", id, "java"));
            }
        } catch (Exception e) {
            result.setException(e);
        }
        result.setUseTime(System.currentTimeMillis() - startTime);
        doListenerAfter(context, result);
        return result;
    }

    @Override
    public boolean remove(String id) {
        return cache.remove(id) != null;
    }

    @Override
    public boolean compiled(String id) {
        return cache.containsKey(id);
    }

    @Override
    public ScriptContext getContext(String id) {
        return cache.get(id);
    }

    protected class JavaCodeContext extends ScriptContext {
        private Class codeClass;
        private Executor executor;

        public JavaCodeContext(String id, String md5, Class codeClass, Executor executor) {
            super(id, md5);
            this.codeClass = codeClass;
            this.executor = executor;
        }

        public Class getCodeClass() {
            return codeClass;
        }

        public Executor getExecutor() {
            return executor;
        }
    }

}
