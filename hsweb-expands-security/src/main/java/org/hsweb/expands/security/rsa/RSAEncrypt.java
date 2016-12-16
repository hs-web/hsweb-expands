package org.hsweb.expands.security.rsa;

/**
 * @author zhouhao
 * @TODO
 */
public class RSAEncrypt {
    public RSAPublicEncrypt publicEncrypt() {
        return new RSAPublicEncrypt();
    }

    public RSAPrivateEncrypt privateEncrypt() {
        return new RSAPrivateEncrypt();
    }
}
