package org.hswebframework.expands.script.engine;

/**
 * Created by zhouhao on 16-6-27.
 */
public interface ScriptListener {
    void before(ScriptContext context);

    void after(ScriptContext context, ExecuteResult result);
}
