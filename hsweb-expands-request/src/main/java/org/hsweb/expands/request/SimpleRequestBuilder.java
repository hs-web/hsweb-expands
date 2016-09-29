package org.hsweb.expands.request;

import org.hsweb.expands.request.email.EmailRequest;
import org.hsweb.expands.request.email.EmailSender;
import org.hsweb.expands.request.ftp.FtpRequest;
import org.hsweb.expands.request.ftp.simple.SimpleFtpRequest;
import org.hsweb.expands.request.http.HttpRequest;
import org.hsweb.expands.request.http.simple.SimpleHttpRequest;
import org.hsweb.expands.request.http.simple.SimpleHttpsRequest;
import org.hsweb.expands.request.webservice.WebServiceRequest;
import org.hsweb.expands.request.webservice.simple.CXFWebServiceRequest;
import org.hsweb.expands.request.websocket.WebSocketRequest;

import java.io.IOException;
import java.util.Properties;

public class SimpleRequestBuilder implements RequestBuilder {
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
    public WebServiceRequest webService(String url) throws Exception {
        return new CXFWebServiceRequest(url).init();
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
