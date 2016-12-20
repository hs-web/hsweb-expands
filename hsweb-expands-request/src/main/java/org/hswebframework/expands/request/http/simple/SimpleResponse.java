package org.hswebframework.expands.request.http.simple;


import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.hswebframework.expands.request.http.Response;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhouhao on 16-6-28.
 */
public class SimpleResponse implements Response {
    HttpResponse response;

    public SimpleResponse(HttpResponse response) {
        this.response = response;
    }

    @Override
    public int getCode() {
        return response.getStatusLine().getStatusCode();
    }

    @Override
    public String asString() throws IOException {
        return new String(asBytes());
    }

    @Override
    public byte[] asBytes() throws IOException {
        return EntityUtils.toByteArray(response.getEntity());
    }

    @Override
    public InputStream asStream() throws IOException {
        return response.getEntity().getContent();
    }

    @Override
    public <T> T getNativeResponse() {
        return (T) response;
    }

    @Override
    public String toString() {
        try {
            return asString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
