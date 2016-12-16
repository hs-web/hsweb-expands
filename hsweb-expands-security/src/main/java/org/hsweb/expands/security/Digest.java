package org.hsweb.expands.security;


/**
 * @author zhouhao
 */
public interface Digest {
    Digest digest(String data);

    Digest digest(byte[] data);

    String get();

}
