package org.hsweb.expands.request.http.simple;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by zhouhao on 16-6-23.
 */
public class StringResultHttpRequest extends AbstractHttpRequest<String> {
    public StringResultHttpRequest(String url) {
        super(url);
    }

    @Override
    protected String getResultValue(HttpResponse res) throws IOException {
        HttpEntity entity = res.getEntity();
        return EntityUtils.toString(entity);
    }
}
