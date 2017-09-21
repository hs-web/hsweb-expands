package org.hswebframework.expands.request.webservice.simple;

import org.hswebframework.expands.request.webservice.WebServiceResult;

public class SimpleWebServiceResult implements WebServiceResult {
    Object data;

    public SimpleWebServiceResult(Object data) {
        this.data = data;
    }

    @Override
    public <T> T get() {
        return (T) data;
    }
}
