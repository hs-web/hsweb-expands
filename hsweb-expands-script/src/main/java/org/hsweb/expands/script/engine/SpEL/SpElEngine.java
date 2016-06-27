package org.hsweb.expands.script.engine.SpEL;

import org.hsweb.commons.MD5;
import org.hsweb.expands.script.engine.DynamicScriptEngine;
import org.hsweb.expands.script.engine.ExecuteResult;
import org.hsweb.expands.script.engine.ListenerSupportEngine;
import org.hsweb.expands.script.engine.ScriptContext;
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
public class SpElEngine extends ListenerSupportEngine {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final Map<String, SpelScriptContext> cache = new ConcurrentHashMap<>();

    protected final ExpressionParser parser = new SpelExpressionParser();

    @Override
    public boolean compiled(String id) {
        return cache.containsKey(id);
    }

    @Override
    public void init(String... contents) throws Exception {
    }

    @Override
    public boolean remove(String id) {
        return cache.remove(id) != null;
    }

    @Override
    public ScriptContext getContext(String id) {
        return cache.get(id);
    }

    @Override
    public boolean compile(String id, String code) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("compile SpEL {} : {}", id, code);
        }
        cache.put(id, new SpelScriptContext(id, MD5.defaultEncode(code), parser.parseExpression(code)));
        return false;
    }

    @Override
    public ExecuteResult execute(String id) {
        return execute(id, new HashMap<>());
    }

    @Override
    public ExecuteResult execute(String id, Map<String, Object> param) {
        if (logger.isDebugEnabled()) {
            logger.debug("execute SpEL {} : {}", id, param);
        }
        ExecuteResult result = new ExecuteResult();
        long start = System.currentTimeMillis();
        SpelScriptContext scriptContext = cache.get(id);
        try {
            if (scriptContext != null) {
                doListenerBefore(scriptContext);
                scriptContext = cache.get(id);
                EvaluationContext context = new StandardEvaluationContext(param);
                for (Map.Entry<String, Object> entry : param.entrySet()) {
                    context.setVariable(entry.getKey(), entry.getValue());
                }
                Object obj = scriptContext.getScript().getValue(context);
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
        doListenerAfter(scriptContext, result);
        return result;
    }

    class SpelScriptContext extends ScriptContext {
        private Expression script;

        public SpelScriptContext(String id, String md5, Expression script) {
            super(id, md5);
            this.script = script;
        }

        public Expression getScript() {
            return script;
        }
    }

}
