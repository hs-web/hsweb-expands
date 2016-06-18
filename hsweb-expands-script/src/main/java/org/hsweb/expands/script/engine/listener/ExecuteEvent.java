package org.hsweb.expands.script.engine.listener;


import org.hsweb.expands.script.engine.ExecuteResult;

/**
 * Created by 浩 on 2015-10-27 0027.
 */
public class ExecuteEvent {

    public static final int TYPE_EXECUTE = 1;

    private int type;

    private String scriptId;

    private ExecuteResult executeResult;

    public ExecuteEvent(int type, String scriptId, ExecuteResult executeResult) {
        this.type = type;
        this.scriptId = scriptId;
        this.executeResult = executeResult;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getScriptId() {
        return scriptId;
    }

    public void setScriptId(String scriptId) {
        this.scriptId = scriptId;
    }

    public ExecuteResult getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(ExecuteResult executeResult) {
        this.executeResult = executeResult;
    }
}
