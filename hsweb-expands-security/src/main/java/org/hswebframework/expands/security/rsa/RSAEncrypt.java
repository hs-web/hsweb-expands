package org.hswebframework.expands.security.rsa;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author zhouhao
 */
public class RSAEncrypt {
    RSAPublicKey  publicKey;
    RSAPrivateKey privateKey;
    int           keySize;

    private RSAEncrypt(int keySize) {
        this.keySize = keySize;
    }

    protected void createKey() {
        KeyPairGenerator keyPairGen;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(keySize);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            publicKey = (RSAPublicKey) keyPair.getPublic();
            privateKey = (RSAPrivateKey) keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public RSAPublicEncrypt publicEncrypt() {
        if (publicKey == null) createKey();
        return new RSAPublicEncrypt(publicKey);
    }

    public RSAPrivateEncrypt privateEncrypt() {
        if (privateKey == null) createKey();
        return new RSAPrivateEncrypt(privateKey);
    }

    public RSAPublicEncrypt publicEncrypt(String key) {
        return new RSAPublicEncrypt(key);
    }

    public RSAPrivateEncrypt privateEncrypt(String key) {
        return new RSAPrivateEncrypt(key);
    }

    public RSAPublicEncrypt publicEncrypt(byte[] key) {
        return new RSAPublicEncrypt(key);
    }

    public RSAPrivateEncrypt privateEncrypt(byte[] key) {
        return new RSAPrivateEncrypt(key);
    }

    public static RSAEncrypt get() {
        return get(1024);
    }

    public static RSAEncrypt get(int keySize) {
        return new RSAEncrypt(keySize);
    }

}
