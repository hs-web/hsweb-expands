package org.hsweb.expands.script.engine.ognl;

import ognl.Ognl;
import org.hsweb.expands.script.engine.DynamicScriptEngine;
import org.hsweb.expands.script.engine.ExecuteResult;
import org.hsweb.expands.script.engine.common.listener.CommonScriptExecuteListener;
import org.hsweb.expands.script.engine.listener.ExecuteEvent;
import org.hsweb.expands.script.engine.listener.ScriptExecuteListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 浩 on 2015-10-28 0028.
 */
public class OgnlEngine implements DynamicScriptEngine {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final Map<String, Object> base = new ConcurrentHashMap<>();

    protected static Map<String, CommonScriptExecuteListener> listenerMap = new HashMap<>();

    @Override
    public boolean compiled(String id) {
        return base.containsKey(id);
    }

    @Override
    public void init(String... contents) throws Exception {
    }

    @Override
    public ExecuteResult execute(String id) {
        return execute(id, new HashMap<String, Object>());
    }

    @Override
    public boolean compile(String id, String code) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("compile Ognl {} : {}", id, code);
        }
        base.put(id, Ognl.parseExpression(code));
        return false;
    }

    @Override
    public ExecuteResult execute(String id, Map<String, Object> param) {
        if (logger.isDebugEnabled()) {
            logger.debug("execute Ognl {} : {}", id, param);
        }
        ExecuteResult result = new ExecuteResult();
        long start = System.currentTimeMillis();
        try {
            Object expression = base.get(id);
            if (id != null) {

                Object obj = Ognl.getValue(expression, param, param);
                result.setSuccess(true);
                result.setResult(obj);
            } else {
                result.setSuccess(false);
                result.setResult(null);
                result.setMessage(String.format("Ognl: %s not found!", id));
            }
            long end = System.currentTimeMillis();
            result.setUseTime(end - start);
        } catch (Exception e) {
            logger.error("execute SpEL error", e);
            result.setException(e);
        }
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
