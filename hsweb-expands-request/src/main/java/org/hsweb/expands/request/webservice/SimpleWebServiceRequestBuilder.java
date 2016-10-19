package org.hsweb.expands.request.webservice;

import org.hsweb.expands.request.webservice.simple.CXFWSDLWebServiceRequest;

public class SimpleWebServiceRequestBuilder implements WebServiceRequestBuilder {
    @Override
    public WebServiceRequest wsdl(String wsdl) throws Exception {
        return new CXFWSDLWebServiceRequest(wsdl).init(wsdl);
    }

    @Override
    public WebServiceRequest wsdl(String wsdl, String url) throws Exception {
        return new CXFWSDLWebServiceRequest(url).init(wsdl);
    }
}
