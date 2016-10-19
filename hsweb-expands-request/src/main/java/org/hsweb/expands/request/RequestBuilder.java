package org.hsweb.expands.request;

import org.hsweb.expands.request.email.EmailRequest;
import org.hsweb.expands.request.ftp.FtpRequest;
import org.hsweb.expands.request.http.HttpRequest;
import org.hsweb.expands.request.webservice.WebServiceRequestBuilder;
import org.hsweb.expands.request.websocket.WebSocketRequest;

import java.io.IOException;

public interface RequestBuilder {
    HttpRequest http(String url);

    HttpRequest https(String url);

    FtpRequest ftp(String host, int port, String username, String password) throws IOException;

    FtpRequest ftp(String host, int port) throws IOException;

    FtpRequest ftp(String host) throws IOException;

    WebServiceRequestBuilder webService() throws Exception;

    EmailRequest email();

    WebSocketRequest webSocket(String url);

}
