package org.hswebframework.expands.request.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhouhao on 16-6-24.
 */
public interface Response {
    int getCode();

    String asString() throws IOException;

    byte[] asBytes() throws IOException;

    InputStream asStream() throws IOException;

    <T> T getNativeResponse();

}
