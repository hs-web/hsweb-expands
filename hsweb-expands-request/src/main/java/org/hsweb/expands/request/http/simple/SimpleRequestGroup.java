package org.hsweb.expands.request.http.simple;

import org.apache.cxf.transport.http.Headers;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.hsweb.commons.StringUtils;
import org.hsweb.expands.request.http.HttpRequest;
import org.hsweb.expands.request.http.HttpRequestGroup;
import org.hsweb.expands.request.http.Response;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author zhouhao
 */
public class SimpleRequestGroup implements HttpRequestGroup {
    private Header[] cookies;

    @Override
    public HttpRequestGroup clearCookie() {
        cookies = null;
        return this;
    }

    public String getCookie() {
        return headerToString();
    }

    protected String headerToString() {
        if (cookies != null) {
            return Arrays.stream(cookies).map(Header::getValue).reduce((c1, c2) -> StringUtils.concat(c1, ";", c2)).orElse(null);
        }
        return null;
    }

    @Override
    public HttpRequest request(String url) {
        SimpleHttpRequest request = new SimpleHttpRequest(url) {
            @Override
            protected Response getResultValue(HttpResponse res) throws IOException {
                Header[] headers = res.getHeaders("Set-Cookie");
                if (headers != null && headers.length > 0)
                    cookies = headers;
                return super.getResultValue(res);
            }
        };
        request.cookie(getCookie());
        return request;
    }
}
