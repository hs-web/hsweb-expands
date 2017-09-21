package org.hswebframework.expands.script.engine.ognl;

import ognl.Ognl;
import org.apache.commons.codec.digest.DigestUtils;
import org.hswebframework.expands.script.engine.ExecuteResult;
import org.hswebframework.expands.script.engine.ListenerSupportEngine;
import org.hswebframework.expands.script.engine.ScriptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 浩 on 2015-10-28 0028.
 */
public class OgnlEngine extends ListenerSupportEngine {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final Map<String, OgnlScriptContext> cache = new ConcurrentHashMap<>();

    @Override
    public boolean compiled(String id) {
        return cache.containsKey(id);
    }

    @Override
    public ScriptContext getContext(String id) {
        return cache.get(id);
    }

    @Override
    public boolean remove(String id) {
        return cache.remove(id) != null;
    }

    @Override
    public void init(String... contents) throws Exception {
    }

    @Override
    public ExecuteResult execute(String id) {
        return execute(id, new HashMap<>());
    }

    @Override
    public boolean compile(String id, String code) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("compile Ognl {} : {}", id, code);
        }
        cache.put(id, new OgnlScriptContext(id, DigestUtils.md5Hex(code), Ognl.parseExpression(code)));
        return false;
    }

    @Override
    public ExecuteResult execute(String id, Map<String, Object> param) {
        if (logger.isDebugEnabled()) {
            logger.debug("execute Ognl {} : {}", id, param);
        }
        ExecuteResult result = new ExecuteResult();
        long start = System.currentTimeMillis();
        OgnlScriptContext scriptContext = cache.get(id);
        try {
            if (scriptContext != null) {
                doListenerBefore(scriptContext);
                scriptContext = cache.get(id);
                param = new HashMap<>(param);
                param.putAll(getGlobalVariable());
                Object obj = Ognl.getValue(scriptContext.getScript(), param, param);
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
        doListenerAfter(scriptContext, result);
        return result;
    }

    class OgnlScriptContext extends ScriptContext {
        private Object script;

        public OgnlScriptContext(String id, String md5, Object script) {
            super(id, md5);
            this.script = script;
        }

        public Object getScript() {
            return script;
        }
    }
}
