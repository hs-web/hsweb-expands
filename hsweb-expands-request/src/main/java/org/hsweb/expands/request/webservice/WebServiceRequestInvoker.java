package org.hsweb.expands.request.webservice;

public interface WebServiceRequestInvoker {
    WebServiceResult invoke(Object... param) throws Exception;
}
