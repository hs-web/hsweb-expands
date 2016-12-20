package org.hsweb.expands.security;

import org.hsweb.expands.security.rsa.RSAEncrypt;

/**
 * @author zhouhao
 * @TODO
 */
public abstract class Encrypt {

    public static RSAEncrypt rsa() {
        return RSAEncrypt.get();
    }
}
