package org.hsweb.expands.request;

import org.hsweb.expands.request.ftp.FtpRequest;
import org.hsweb.expands.request.http.HttpRequest;

import java.io.IOException;

/**
 * Created by zhouhao on 16-6-23.
 */
public interface RequestBuilder {
    HttpRequest http(String url);

    HttpRequest https(String url);

    FtpRequest ftp(String host, int port, String username, String password) throws IOException;

    FtpRequest ftp(String host, int port) throws IOException;

    FtpRequest ftp(String host) throws IOException;
}
