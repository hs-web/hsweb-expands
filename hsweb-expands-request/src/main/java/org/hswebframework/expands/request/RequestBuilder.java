package org.hswebframework.expands.request;

import org.hswebframework.expands.request.email.EmailRequest;
import org.hswebframework.expands.request.ftp.FtpRequest;
import org.hswebframework.expands.request.http.HttpRequest;
import org.hswebframework.expands.request.http.HttpRequestGroup;
import org.hswebframework.expands.request.webservice.WebServiceRequestBuilder;
import org.hswebframework.expands.request.websocket.WebSocketRequest;

import java.io.IOException;

public interface RequestBuilder {
    HttpRequestGroup http();

    HttpRequest http(String url);

    HttpRequest https(String url);

    FtpRequest ftp(String host, int port, String username, String password) throws IOException;

    FtpRequest ftp(String host, int port) throws IOException;

    FtpRequest ftp(String host) throws IOException;

    WebServiceRequestBuilder webService() throws Exception;

    EmailRequest email();

    WebSocketRequest webSocket(String url);

}
