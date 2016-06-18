package org.hsweb.expands.script.engine.common.listener;


import org.hsweb.expands.script.engine.listener.ExecuteEvent;
import org.hsweb.expands.script.engine.listener.ScriptExecuteListener;

public interface CommonScriptExecuteListener extends ScriptExecuteListener {
    void onExecute(ExecuteEvent event);
}
