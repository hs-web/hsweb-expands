package org.hsweb.expands.request.webservice;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public interface WebServiceRequest {

    WebServiceRequest init() throws Exception;

    WebServiceRequestInvoker request(String interfaceName, String service, String method) throws NoSuchMethodException;

    WebServiceRequestInvoker request(String method) throws NoSuchMethodException;

    WebServiceRequestInvoker request() throws NoSuchMethodException;

    List<String> services();

    List<String> interfaces();

    Method[] methods(String interfaceName);
}
