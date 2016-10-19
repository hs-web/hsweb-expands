package org.hsweb.expands.request.webservice;

public interface WebServiceRequestBuilder {
    WebServiceRequest wsdl(String wsdl) throws Exception;

    WebServiceRequest wsdl(String wsdl, String url) throws Exception;

}
