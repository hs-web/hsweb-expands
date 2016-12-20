package org.hsweb.expands.security;


/**
 * @author zhouhao
 */
public interface Digest {
    Digest digest(String data);

    Digest digest(byte[] data);

    String get();

    Digest MD5  = new DefaultMessageDigest("md5");
    Digest SHA1 = new DefaultMessageDigest("sha1");
}
