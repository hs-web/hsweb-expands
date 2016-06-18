package org.hsweb.expands.script.engine.common;

import org.hsweb.expands.script.engine.DynamicScriptEngine;
import org.hsweb.expands.script.engine.ExecuteResult;
import org.hsweb.expands.script.engine.common.listener.CommonScriptExecuteListener;
import org.hsweb.expands.script.engine.listener.ExecuteEvent;
import org.hsweb.expands.script.engine.listener.ScriptExecuteListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 浩 on 2015-10-27 0027.
 */
public abstract class CommonScriptEngine implements DynamicScriptEngine {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 编译器
    protected Compilable compilable;
    // 脚本库
    protected Map<String, CompiledScript> scriptBase = new ConcurrentHashMap<>();

    protected Map<String, CommonScriptExecuteListener> listenerMap = new HashMap<>();

    protected Bindings utilBindings;

    public abstract String getScriptName();

    @Override
    public boolean compiled(String id) {
        return scriptBase.containsKey(id);
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
        CompiledScript wbScript;
        for (String content : contents) {
            wbScript = compilable.compile(content);
            wbScript.eval(utilBindings);
        }
        scriptBase.clear();
    }

    @Override
    public boolean compile(String id, String code) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("compile {} {} : {}", getScriptName(), id, code);
        }
        if (compilable == null)
            init();
        CompiledScript compiledScript = compilable.compile(code);
        scriptBase.put(id, compiledScript);
        return true;
    }

    @Override
    public ExecuteResult execute(String id) {
        return execute(id, new HashMap<String, Object>());
    }

    @Override
    public ExecuteResult execute(String id, Map<String, Object> param) {
        long startTime = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("execute {} {} : {}", getScriptName(), id, param);
        }
        ExecuteResult result = new ExecuteResult();
        try {
            CompiledScript compiledScript = scriptBase.get(id);
            if (compiledScript != null) {
                ScriptContext context = new SimpleScriptContext();
                context.setBindings(utilBindings, ScriptContext.GLOBAL_SCOPE);
                for (Map.Entry<String, Object> entry : param.entrySet()) {
                    context.setAttribute(entry.getKey(), entry.getValue(), ScriptContext.ENGINE_SCOPE);
                }
                result.setResult(compiledScript.eval(context));
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

}
