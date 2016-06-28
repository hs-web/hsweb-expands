package org.hsweb.expands.request;

import org.hsweb.expands.request.ftp.FtpRequest;
import org.hsweb.expands.request.ftp.simple.SimpleFtpRequest;
import org.hsweb.expands.request.http.HttpRequest;
import org.hsweb.expands.request.http.simple.SimpleHttpRequest;

import java.io.IOException;

/**
 * Created by zhouhao on 16-6-23.
 */
public class SimpleRequestBuilder implements RequestBuilder {
    @Override
    public HttpRequest http(String url) {
        if (!url.startsWith("http")) url = "http://" + url;
        return new SimpleHttpRequest(url);
    }

    public HttpRequest https(String url) {
        if (!url.startsWith("http")) url = "https://" + url;
        try {
            return new SimpleHttpRequest(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FtpRequest ftp(String host, int port, String username, String password) throws IOException {
        return new SimpleFtpRequest(host, port, username, password);
    }

    @Override
    public FtpRequest ftp(String host, int port) throws IOException {
        return ftp(host, port, null, null);
    }

    @Override
    public FtpRequest ftp(String host) throws IOException {
        return ftp(host, 22);
    }

}
