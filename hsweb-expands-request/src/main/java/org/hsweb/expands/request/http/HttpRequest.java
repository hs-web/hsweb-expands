package org.hsweb.expands.request.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * http请求
 * Created by zhouhao on 16-6-23.
 */
public interface HttpRequest<R> extends Closeable {

    HttpRequest<R> before(Callback<HttpUriRequest> callback);

    HttpRequest<R> after(Callback<HttpResponse> callback);

    HttpRequest<R> encode(String encode);

    HttpRequest<R> contentType(String type);

    HttpRequest<R> param(String name, String value);

    HttpRequest<R> params(Map<String, String> params);

    HttpRequest<R> header(String name, String value);

    HttpRequest<R> headers(Map<String, String> headers);

    HttpRequest<R> requestBody(String body);

    HttpRequest<R> resultAsJsonString();

    HttpRequest<R> cookie(String cookie);

    HttpDownloader<R> download() throws IOException;

    R upload(String paramName, File file) throws IOException;

    R upload(File file) throws IOException;

    R get() throws IOException;

    R post() throws IOException;

    R put() throws IOException;

    R delete() throws IOException;

    R patch() throws IOException;
}
