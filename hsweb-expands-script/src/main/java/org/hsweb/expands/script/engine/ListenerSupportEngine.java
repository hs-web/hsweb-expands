package org.hsweb.expands.script.engine;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhouhao on 16-6-27.
 */
public abstract class ListenerSupportEngine implements DynamicScriptEngine {
    protected List<ScriptListener> listener = new LinkedList<>();

    protected void doListenerBefore(ScriptContext context) {
        listener.forEach(listener -> listener.before(context));
    }

    protected void doListenerAfter(ScriptContext context, ExecuteResult result) {
        listener.forEach(listener -> listener.after(context, result));
    }

    @Override
    public void addListener(ScriptListener scriptListener) {
        listener.add(scriptListener);
    }
}
