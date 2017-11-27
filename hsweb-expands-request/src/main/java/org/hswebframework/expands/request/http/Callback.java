package org.hswebframework.expands.request.http;

import org.apache.http.HttpMessage;

/**
 * Created by zhouhao on 16-6-24.
 */
public interface Callback<C extends HttpMessage> {
    void accept(C message);
}
