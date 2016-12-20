package org.hswebframework.expands.security.rsa;

import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author zhouhao
 */
public class RSAPublicEncrypt extends DefaultEncrypt {

    private byte[] publicKey;

    public RSAPublicEncrypt(RSAPublicKey publicKey) {
        this.publicKey = Base64.encodeBase64(publicKey.getEncoded());
    }

    public RSAPublicEncrypt(String publicKey) {
        this.publicKey = publicKey.getBytes();
    }

    public RSAPublicEncrypt(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public String getKey() {
        return new String(publicKey);
    }

    @Override
    protected EncodedKeySpec encodedKeySpec(byte[] keyBytes) {
        return new X509EncodedKeySpec(keyBytes);
    }

    public boolean verify(String sign, byte[] data) {
        try {
            byte[] keyBytes = Base64.decodeBase64(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicK = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicK);
            signature.update(data);
            return signature.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
