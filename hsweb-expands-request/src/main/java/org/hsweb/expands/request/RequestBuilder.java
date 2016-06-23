package org.hsweb.expands.request;

import org.hsweb.expands.request.http.HttpRequest;

/**
 * Created by zhouhao on 16-6-23.
 */
public interface RequestBuilder {
    HttpRequest<String> http(String url);
}
