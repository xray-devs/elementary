package com.elementary.core.util;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * @author Mr丶s
 * @ClassName RSAUtil
 * @Version V1.0
 * @Date 2018/12/6 22:37
 * @Description
 */
@Component
public class RSAUtil {

    // 公钥
    private String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSwLX8k2DOWp4kwYTwRQS7MBAMMK2SOovXpeoRAqsbqP3OO+H4lmnV9hNKr2HbKyDjInKNKGdGrAXmXjpOJRONkrpUOV8Uu6IW822HOEJrDkmjjXSFd/gThklGcjonanlr47Qx3ogtMuzR7nXzLxM3WA9hxGbVB/gNUgjkjq4XVQIDAQAB";
    // 私钥
    private String privateKeyStr = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJLAtfyTYM5aniTBhPBFBLswEAwwrZI6i9el6hECqxuo/c474fiWadX2E0qvYdsrIOMico0oZ0asBeZeOk4lE42SulQ5XxS7ohbzbYc4QmsOSaONdIV3+BOGSUZyOidqeWvjtDHeiC0y7NHudfMvEzdYD2HEZtUH+A1SCOSOrhdVAgMBAAECgYB+CsZDiNE8atgo+Y0UUDMgEse1sJljXqV9MiM3OPN9bkePOh97QrTj79Xh74A8nW5BpvR8CJerO1RV59hsBawkPGu033FFk3vA/lMD1l5MbD0Bpfmk5cYc8mfB7lIHj7lIJf+wEeatzvPk6j+lkgfRJCVSmKJRIkLPMvBHybXoAQJBAN0UKE/G2+b2RRuAvDy9qrV8hihEvvNZLOwRAZUv9Ughmgg0ZgQg5T0CQ6mgLbxMfVXaMaaExFDX8P/4Pyhn/bECQQCp7wHNL9lWMXmswUR2oiMeZd2i6nNF/VeSxqhPfKJV3AEpCPADDOde/IMSKtav102srNyWcDXzuUgshM8M7KjlAkAHuZaXAwnYDi7oAR2SucXnRYyih5RsiGcrcISvo9dcR+BL/Ri7eDLKaPPYT4KJm9qdT1yAxw7dDE6dgx0h2lBhAkBzQPBG2xob1IiCu74UfBk3h71PvpWaNZ5MpUIITy7G7uU2kKJAX2MZUEgRbU6Yin1bDQ67VbWx7Mtfrl/Dk0tlAkBm91M363IT0g2d3zw+FGpTVBboepMVJas0SC608MJhGEmvwg3/ezHKz3OSo3HeQmXpcl10zmouXJhHaZGGkzpy";

    private static RSAUtil ourInstance = new RSAUtil();

    public static RSAUtil getInstance() { return ourInstance; }

    // 生成密钥对
    private void generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator;
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 获取公钥，并以base64格式打印出来
        PublicKey publicKey = keyPair.getPublic();
        publicKeyStr = new String(Base64.getEncoder().encode(publicKey.getEncoded()));

        // 获取私钥，并以base64格式打印出来
        PrivateKey privateKey = keyPair.getPrivate();
        privateKeyStr = new String(Base64.getEncoder().encode(privateKey.getEncoded()));

    }

    // 将base64编码后的公钥字符串转成PublicKey实例
    private PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    // 将base64编码后的私钥字符串转成PrivateKey实例
    private PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    // 公钥加密
    public String encryptByPublicKey(String content) throws Exception {
        // 获取公钥
        PublicKey publicKey = getPublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = cipher.doFinal(content.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    // 私钥加密
    public String encryptByPrivateKey(String content) throws Exception {
        // 获取私钥
        PrivateKey privateKey = getPrivateKey(privateKeyStr);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] cipherText = cipher.doFinal(content.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    // 私钥解密
    public String decryptByPrivateKey(String content) throws Exception {
        // 获取私钥
        PrivateKey privateKey = getPrivateKey(privateKeyStr);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] cipherText = Base64.getDecoder().decode(content);
        byte[] decryptText = cipher.doFinal(cipherText);
        return new String(decryptText);
    }

    // 公钥解密
    public String decryptByPublicKey(String content) throws Exception {
        // 获取公钥
        PublicKey publicKey = getPublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] cipherText = Base64.getDecoder().decode(content);
        byte[] decryptText = cipher.doFinal(cipherText);
        return new String(decryptText);
    }

    @Test
    public void test2() throws Exception {

        // 公钥加密
        String encryptedBytes = encryptByPublicKey("{aaa:aaa}");
        System.out.println("公钥加密后：" + encryptedBytes);
        // 私钥解密
        String decryptedBytes = decryptByPrivateKey(encryptedBytes);
        System.out.println("私钥解密后：" + decryptedBytes);


        // 私钥加密
        String encryptedBytes2 = encryptByPrivateKey("{aaa:aaa}");
        System.out.println("私钥加密后：" + encryptedBytes2);
        // 公钥解密
        String decryptedBytes2 = decryptByPublicKey(encryptedBytes2);
        System.out.println("公钥解密后：" + decryptedBytes2);
    }





}
