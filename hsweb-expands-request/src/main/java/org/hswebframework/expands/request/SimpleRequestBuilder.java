package org.hswebframework.expands.request;

import org.hswebframework.expands.request.email.EmailRequest;
import org.hswebframework.expands.request.ftp.FtpRequest;
import org.hswebframework.expands.request.ftp.simple.SimpleFtpRequest;
import org.hswebframework.expands.request.http.HttpRequest;
import org.hswebframework.expands.request.http.HttpRequestGroup;
import org.hswebframework.expands.request.http.simple.SimpleHttpRequest;
import org.hswebframework.expands.request.http.simple.SimpleHttpsRequest;
import org.hswebframework.expands.request.http.simple.SimpleRequestGroup;
import org.hswebframework.expands.request.webservice.SimpleWebServiceRequestBuilder;
import org.hswebframework.expands.request.webservice.WebServiceRequestBuilder;
import org.hswebframework.expands.request.websocket.WebSocketRequest;

import java.io.IOException;

public class SimpleRequestBuilder implements RequestBuilder {

    @Override
    public HttpRequestGroup http() {
        return new SimpleRequestGroup();
    }

    @Override
    public HttpRequest http(String url) {
        if (!url.startsWith("http")) url = "http://" + url;
        return new SimpleHttpRequest(url);
    }

    public HttpRequest https(String url) {
        if (!url.startsWith("http")) url = "https://" + url;
        try {
            return new SimpleHttpsRequest(url);
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

    @Override
    public WebServiceRequestBuilder webService() throws Exception {
        return new SimpleWebServiceRequestBuilder();
    }


    @Override
    public EmailRequest email() {
        // TODO: 16-9-29
        throw new UnsupportedOperationException();
    }

    @Override
    public WebSocketRequest webSocket(String url) {
        // TODO: 16-9-29
        throw new UnsupportedOperationException();
    }

}
