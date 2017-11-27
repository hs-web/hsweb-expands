package org.hswebframework.expands.security;

import org.hswebframework.expands.security.rsa.RSAEncrypt;

/**
 * @author zhouhao
 */
public abstract class Encrypt {

    public static RSAEncrypt rsa() {
        return RSAEncrypt.get();
    }
}
