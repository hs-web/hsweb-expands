package org.hsweb.expands.security;


import java.security.*;

/**
 * @author zhouhao
 */
public class DefaultMessageDigest implements Digest {
    java.security.MessageDigest messageDigest;

    public DefaultMessageDigest(String name) {
        try {
            messageDigest = java.security.MessageDigest.getInstance(name);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Digest digest(String data) {
        return digest(data.getBytes());
    }

    @Override
    public Digest digest(byte[] data) {
        messageDigest.update(data);
        return this;
    }

    @Override
    public String get() {
        StringBuilder builder = new StringBuilder();
        byte[] hashValue = messageDigest.digest();
        for (int i = 0; i < hashValue.length; i++) {
            String shaHex = Integer.toHexString(hashValue[i] & 0xFF);
            if (shaHex.length() < 2) {
                builder.append(0);
            }
            builder.append(shaHex);
        }
        return builder.toString();
    }

}
