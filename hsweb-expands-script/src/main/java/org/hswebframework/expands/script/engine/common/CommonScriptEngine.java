package org.hswebframework.expands.script.engine.common;

import org.apache.commons.codec.digest.DigestUtils;
import org.hswebframework.expands.script.engine.ExecuteResult;
import org.hswebframework.expands.script.engine.ListenerSupportEngine;
import org.hswebframework.expands.script.engine.ScriptListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CommonScriptEngine extends ListenerSupportEngine {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    // 编译器
    protected Compilable compilable;
    // 脚本库
    protected Map<String, CommonScriptContext> scriptBase = new ConcurrentHashMap<>();

    protected Bindings utilBindings;

    protected List<ScriptListener> scriptListeners;

    public abstract String getScriptName();

    @Override
    public boolean compiled(String id) {
        return scriptBase.containsKey(id);
    }

    @Override
    public boolean remove(String id) {
        return scriptBase.remove(id) != null;
    }

    public CommonScriptEngine() {
        try {
            init();
        } catch (Exception e) {
            logger.warn("init {} error", getScriptName());
        }
    }

    @Override
    public void init(String... contents) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName(getScriptName());
        compilable = (Compilable) engine;
        utilBindings = engine.createBindings();
        CompiledScript script;
        for (String content : contents) {
            script = compilable.compile(content);
            script.eval(utilBindings);
        }
        //scriptBase.clear();
    }

    @Override
    public org.hswebframework.expands.script.engine.ScriptContext getContext(String id) {
        return scriptBase.get(id);
    }

    @Override
    public boolean compile(String id, String code) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("compile {} {} : {}", getScriptName(), id, code);
        }
        if (compilable == null)
            init();
        CompiledScript compiledScript = compilable.compile(code);
        CommonScriptContext scriptContext = new CommonScriptContext(id, DigestUtils.md5Hex(code), compiledScript);
        scriptBase.put(id, scriptContext);
        return true;
    }

    @Override
    public void addListener(ScriptListener scriptListener) {
        if (scriptListeners == null) {
            scriptListeners = new LinkedList<>();
        }
        scriptListeners.add(scriptListener);
    }

    @Override
    public ExecuteResult execute(String id) {
        return execute(id, new HashMap<>());
    }

    @Override
    public ExecuteResult execute(String id, Map<String, Object> param) {
        long startTime = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("execute {} {} : {}", getScriptName(), id);
        }
        ExecuteResult result = new ExecuteResult();
        CommonScriptContext scriptContext = scriptBase.get(id);
        try {
            if (scriptContext != null) {
                doListenerBefore(scriptContext);
                ScriptContext context = new SimpleScriptContext();
                context.setBindings(utilBindings, ScriptContext.GLOBAL_SCOPE);

                for (Map.Entry<String, Object> entry : param.entrySet()) {
                    context.setAttribute(entry.getKey(), entry.getValue(), ScriptContext.ENGINE_SCOPE);
                }
                result.setResult(scriptContext.getScript().eval(context));
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
                result.setResult(null);
                result.setMessage(String.format("script(%s): %s not found!", getScriptName(), id));
            }
        } catch (ScriptException e) {
            result.setException(e);
        }
        result.setUseTime(System.currentTimeMillis() - startTime);
        doListenerAfter(scriptContext, result);
        return result;
    }

    @Override
    public void addGlobalVariable(Map<String, Object> vars) {
        utilBindings.putAll(vars);
    }

    protected class CommonScriptContext extends org.hswebframework.expands.script.engine.ScriptContext {
        private CompiledScript script;

        public CommonScriptContext(String id, String md5, CompiledScript script) {
            super(id, md5);
            this.script = script;
        }

        public CompiledScript getScript() {
            return script;
        }
    }

    public Bindings getUtilBindings() {
        return utilBindings;
    }

    public void setUtilBindings(Bindings utilBindings) {
        this.utilBindings = utilBindings;
    }
}
