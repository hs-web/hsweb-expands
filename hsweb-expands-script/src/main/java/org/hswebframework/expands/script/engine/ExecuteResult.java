package org.hswebframework.expands.script.engine;

import javax.script.ScriptException;
import java.util.function.Supplier;

public class ExecuteResult {
    private boolean success;

    private Object result;

    private String message;

    private transient Exception exception;

    private long useTime;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * use {@link this#get()} or {@link this#getIfSuccess()}
     *
     * @return
     */
    @Deprecated
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

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
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

    public Object get() {
        return result;
    }

    public Object getIfSuccess() throws Exception {
        if (!success) {
            if (exception != null) {
                throw exception;
            } else
                throw new ScriptException(message);
        }
        return result;
    }

    public Object getIfSuccess(Object defaultValue) {
        if (!success) {
            return defaultValue;
        }
        return result;
    }

    public Object getIfSuccess(Supplier<?> supplier) {
        if (!success) {
            return supplier.get();
        }
        return result;
    }

}
