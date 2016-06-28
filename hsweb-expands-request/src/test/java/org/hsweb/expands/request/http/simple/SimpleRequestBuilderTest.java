package org.hsweb.expands.request.http.simple;


import org.hsweb.expands.request.RequestBuilder;
import org.hsweb.expands.request.SimpleRequestBuilder;
import org.hsweb.expands.request.ftp.FtpRequest;
import org.hsweb.expands.request.http.HttpRequest;
import org.hsweb.expands.request.http.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by zhouhao on 16-6-23.
 */
public class SimpleRequestBuilderTest {

    RequestBuilder builder;

    @Before
    public void setup() {
        builder = new SimpleRequestBuilder();
    }

    @Test
    public void testHttp() throws IOException {
        HttpRequest request = builder.http("192.168.2.195:8888/user");
        Response response=request.get();
        System.out.println(response.getCode());
        System.out.println(response.asString());
    }

    @Test
    public void testFtp() throws IOException {
        FtpRequest request = builder.ftp("192.168.2.142", 2121);
        request.encode("gbk");
        request.ls().forEach(System.out::println);
    }


}