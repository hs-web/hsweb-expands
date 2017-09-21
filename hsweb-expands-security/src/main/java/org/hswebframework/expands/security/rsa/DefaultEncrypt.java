package org.hswebframework.expands.security.rsa;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.EncodedKeySpec;

/**
 * TODO 完成注释
 *
 * @author zhouhao
 */
public abstract class DefaultEncrypt {
    /**
     * RSA最大加密明文大小
     */
    static final int    MAX_ENCRYPT_BLOCK   = 117;
    /**
     * RSA最大解密密文大小
     */
    static final int    MAX_DECRYPT_BLOCK   = 128;
    /**
     * 签名算法
     */
    static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    protected abstract String getKey();

    protected abstract EncodedKeySpec encodedKeySpec(byte[] keyBytes);

    public byte[] encrypt(byte[] data) {
        try {
            byte[] keyBytes = Base64.decodeBase64(getKey());
            EncodedKeySpec keySpec = encodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            Key key = (this instanceof RSAPrivateEncrypt)
                    ? keyFactory.generatePrivate(keySpec)
                    : keyFactory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return doFinal(data, cipher, MAX_ENCRYPT_BLOCK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] doFinal(byte[] data, Cipher cipher, int max) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int inputLen = data.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > max) {
                    cache = cipher.doFinal(data, offSet, max);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * max;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        }
    }

    public byte[] decrypt(byte[] data) {
        try {
            byte[] keyBytes = Base64.decodeBase64(getKey());
            EncodedKeySpec keySpec = encodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            Key key = (this instanceof RSAPrivateEncrypt)
                    ? keyFactory.generatePrivate(keySpec)
                    : keyFactory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, key);
            return doFinal(data, cipher, MAX_DECRYPT_BLOCK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
