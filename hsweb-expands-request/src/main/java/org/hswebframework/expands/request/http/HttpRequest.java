package org.hswebframework.expands.request.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * http请求
 * Created by zhouhao on 16-6-23.
 */
public interface HttpRequest extends Closeable {

    HttpRequest before(Callback<HttpUriRequest> callback);

    HttpRequest after(Callback<HttpResponse> callback);

    HttpRequest encode(String encode);

    HttpRequest contentType(String type);

    HttpRequest param(String name, String value);

    HttpRequest params(Map<String, String> params);

    HttpRequest header(String name, String value);

    HttpRequest headers(Map<String, String> headers);

    HttpRequest requestBody(String body);

    HttpRequest resultAsJsonString();

    HttpRequest cookie(String cookie);

    HttpDownloader download() throws IOException;

    Response upload(String paramName, File file) throws IOException;

    Response upload(String paramName,InputStream inputStream) throws IOException;

    Response upload(File file) throws IOException;

    Response get() throws IOException;

    Response post() throws IOException;

    Response put() throws IOException;

    Response delete() throws IOException;

    Response patch() throws IOException;
}
