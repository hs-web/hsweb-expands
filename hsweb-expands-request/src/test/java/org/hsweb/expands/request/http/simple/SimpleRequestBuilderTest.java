package org.hsweb.expands.request.http.simple;


import org.hsweb.expands.request.RequestBuilder;
import org.hsweb.expands.request.SimpleRequestBuilder;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by zhouhao on 16-6-23.
 */
public class SimpleRequestBuilderTest {
    @Test
    public void testSimple() throws IOException {
        RequestBuilder builder = new SimpleRequestBuilder();
        String str = builder.http("http://www.baidu.com").get();
        System.out.println(str);
    }
}