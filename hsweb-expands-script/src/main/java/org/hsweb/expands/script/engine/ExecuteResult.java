package org.hsweb.expands.script.engine;

/**
 * Created by 浩 on 2015-10-27 0027.
 */
public class ExecuteResult {
    private boolean success;

    private Object result;

    private String message;

    private transient Throwable exception;

    private long useTime;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getMessage() {
        if (message == null && exception != null) {
            message = exception.getMessage();
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    @Override
    public String toString() {
        return String.valueOf(this.getResult());
    }
}
