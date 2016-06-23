package org.hsweb.expands.request;

import org.hsweb.expands.request.http.HttpRequest;
import org.hsweb.expands.request.http.simple.StringResultHttpClientRequest;

/**
 * Created by zhouhao on 16-6-23.
 */
public class SimpleRequestBuilder implements RequestBuilder {
    @Override
    public HttpRequest<String> http(String url) {
        return new StringResultHttpClientRequest(url);
    }
}
