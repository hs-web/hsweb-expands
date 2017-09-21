package org.hswebframework.expands.request.webservice;

public interface WebServiceRequestInvoker {
    WebServiceResult invoke(Object... param) throws Exception;
}
