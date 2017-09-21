package org.hswebframework.expands.security.rsa;


import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * @author zhouhao
 */
public class RSAPrivateEncrypt extends DefaultEncrypt {
    private byte[] privateKey;

    public RSAPrivateEncrypt(RSAPrivateKey privateKey) {
        this.privateKey = Base64.encodeBase64(privateKey.getEncoded());
    }

    public RSAPrivateEncrypt(String privateKey) {
        this.privateKey = privateKey.getBytes();
    }

    public RSAPrivateEncrypt(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    public String getKey() {
        return new String(privateKey);
    }

    @Override
    protected EncodedKeySpec encodedKeySpec(byte[] keyBytes) {
        return new PKCS8EncodedKeySpec(keyBytes);
    }

    public String sign(byte[] data) {
        try {
            byte[] keyBytes = Base64.decodeBase64(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateK);
            signature.update(data);
            return new String(Base64.encodeBase64(signature.sign()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
