package org.hswebframework.expands.security;

import org.hswebframework.expands.security.rsa.RSAEncrypt;
import org.hswebframework.expands.security.rsa.RSAPrivateEncrypt;
import org.hswebframework.expands.security.rsa.RSAPublicEncrypt;
import org.junit.Assert;
import org.junit.Test;

/**
 * TODO 完成注释
 *
 * @author zhouhao
 */
public class RSATests {

    @Test
    public void simpleTest() {
        byte[] data = "hsweb加密".getBytes();

        //不传参数，自动生成key
        RSAEncrypt encrypt = Encrypt.rsa();
        RSAPublicEncrypt publicEncrypt = encrypt.publicEncrypt();
        RSAPrivateEncrypt privateEncrypt = encrypt.privateEncrypt();
        System.out.println(publicEncrypt.getKey());
        System.out.println(privateEncrypt.getKey());
        data = privateEncrypt.encrypt(data);
        data = publicEncrypt.decrypt(data);

        data = publicEncrypt.encrypt(data);
        data = privateEncrypt.decrypt(data);
        Assert.assertEquals(new String(data), "hsweb加密");
    }

    @Test
    public void createKeyAndPublicEncryptPrivateDecrypt() {
        byte[] data = "hsweb加密".getBytes();

        RSAEncrypt encrypt = Encrypt.rsa();
        //不传参数，自动生成key
        RSAPublicEncrypt publicEncrypt = encrypt.publicEncrypt();
        RSAPrivateEncrypt privateEncrypt = encrypt.privateEncrypt();

        System.out.println(String.format("publicKey: %s", publicEncrypt.getKey()));
        System.out.println(String.format("privateKey: %s", privateEncrypt.getKey()));
        //签名
        String sign = privateEncrypt.sign(data);
        System.out.println(String.format("sign:%s", sign));
        byte[] encryptData = publicEncrypt.encrypt(data);
        boolean verify = publicEncrypt.verify(sign, data);
        Assert.assertTrue(verify);

        byte[] decryptData = privateEncrypt.decrypt(encryptData);
        Assert.assertEquals(new String(decryptData), "hsweb加密");

    }

    @Test
    public void useExistKeyAndPublicEncryptPrivateDecrypt() {
        byte[] data = "hsweb加密".getBytes();

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQqwnUk+EET8WskYT2m16kSgzT1x1b1XNqMMdh/w9roFTfOWyZwQ1ItzztVYR+a9ja10pCJG5ahjWoHOowzSbIpqGG3hIY/3SGATYoQUIvbp6JUPiORueRcCVDtmC0HETCsvN+pftsHDNdbquk7tyq9OGaOSGKlRFoW4doqK2OkwIDAQAB\n";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJCrCdST4QRPxayRhPabXqRKDNPXHVvVc2owx2H/D2ugVN85bJnBDUi3PO1VhH5r2NrXSkIkblqGNagc6jDNJsimoYbeEhj/dIYBNihBQi9unolQ+I5G55FwJUO2YLQcRMKy836l+2wcM11uq6Tu3Kr04Zo5IYqVEWhbh2iorY6TAgMBAAECgYBZj9oFJgvvfGd3n5t+hM/BzCpG1+1QFkReoJY/QAXO7rK8g7ONOKw9sVth30iob6IQUaqiaiV7m40KZ+RAlDGGpYPKmGPXpdEvpwuFZ56xHI/48sds8rB/62V7eIlEm5HxWTgteKYBDQaNogqr0j+18ZlMaIKbAl4powkjPY3yKQJBANc5SXE6X+HzRi9APSfnomm7CY0Lk8M0TjuKoWriStBS2AlIp0NOtxhqBXGuj2VVeKKJPsPU79gbBDTX2F4c6Q8CQQCsE68hLM09H5epQZYzyLmIJaVXNWvFdMsiWQijAOSraYwmZ7WBAIAr7KkoXYPMWZIONMIkXz1hmSD3D+9Y+Zo9AkAmT6pFH5EF1Zo+uv6n5dHBZGv00YTCEMOEUc8eCZ4rqzONo4MytgZdsDG75MjdzvMka63iijsPiu+awHlhd1/9AkBaKM4f0buPhRgPpL+wTkF3plHlSaY4BSPR0ViHH5awgVLfZINjHgIKAav37Fd8IIo4S0hVilk84+Cz0nOT6OP5AkBYFzfIJK9FFhps8oLLy/JTtqIBFaQupWv0pQspLBWh0fAtXvAbY0S5EdX/LcvZD9PmC/Facru40oRB5uYKgDzT";

        RSAEncrypt encrypt = Encrypt.rsa();

        RSAPublicEncrypt publicEncrypt = encrypt.publicEncrypt(publicKey);
        RSAPrivateEncrypt privateEncrypt = encrypt.privateEncrypt(privateKey);

        String sign = privateEncrypt.sign(data);
        System.out.println(String.format("sign:%s", sign));
        byte[] encryptData = publicEncrypt.encrypt(data);
        boolean verify = publicEncrypt.verify(sign, data);

        System.out.println(String.format("verify:%s", verify));

        byte[] decryptData = privateEncrypt.decrypt(encryptData);
        Assert.assertEquals(new String(decryptData), "hsweb加密");
    }
}
