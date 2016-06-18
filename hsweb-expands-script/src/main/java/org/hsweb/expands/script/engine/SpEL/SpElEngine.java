package org.hsweb.expands.script.engine.SpEL;

import org.hsweb.expands.script.engine.DynamicScriptEngine;
import org.hsweb.expands.script.engine.ExecuteResult;
import org.hsweb.expands.script.engine.common.listener.CommonScriptExecuteListener;
import org.hsweb.expands.script.engine.listener.ExecuteEvent;
import org.hsweb.expands.script.engine.listener.ScriptExecuteListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 浩 on 2015-10-28 0028.
 */
public class SpElEngine implements DynamicScriptEngine {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final Map<String, Expression> base = new ConcurrentHashMap<>();

    protected final ExpressionParser parser = new SpelExpressionParser();

    protected static Map<String, CommonScriptExecuteListener> listenerMap = new HashMap<>();

    @Override
    public boolean compiled(String id) {
        return base.containsKey(id);
    }

    @Override
    public void init(String... contents) throws Exception {
    }

    @Override
    public boolean compile(String id, String code) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("compile SpEL {} : {}", id, code);
        }
        base.put(id, parser.parseExpression(code));
        return false;
    }

    @Override
    public ExecuteResult execute(String id) {
        return execute(id, new HashMap<String, Object>());
    }

    @Override
    public ExecuteResult execute(String id, Map<String, Object> param) {
        if (logger.isDebugEnabled()) {
            logger.debug("execute SpEL {} : {}", id, param);
        }
        ExecuteResult result = new ExecuteResult();
        long start = System.currentTimeMillis();
        try {
            Expression expression = base.get(id);
            if (id != null) {
                EvaluationContext context = new StandardEvaluationContext(param);
                for (Map.Entry<String, Object> entry : param.entrySet()) {
                    context.setVariable(entry.getKey(), entry.getValue());
                }
                Object obj = expression.getValue(context);
                result.setSuccess(true);
                result.setResult(obj);
            } else {
                result.setSuccess(false);
                result.setResult(null);
                result.setMessage(String.format("SpEL: %s not found!", id));
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
