package com.elementary.core.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author Mr丶s
 * @ClassName RSAkeyCreate
 * @Version V1.0
 * @Date 2018/12/6 23:13
 * @Description
 */
@Component
public class RSAkeyCreate {
    @Test
    public void test(){
        //随机生成密钥对
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 初始化密钥对生成器，密钥大小为96-1024位
        if (keyPairGen != null) {
            keyPairGen.initialize(1024, new SecureRandom());
        }
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen != null ? keyPairGen.generateKeyPair() : null;
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) (keyPair != null ? keyPair.getPrivate() : null);
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) (keyPair != null ? keyPair.getPublic() : null);


        String publicKeyString = Base64.encode( publicKey != null ? publicKey.getEncoded() : new byte[0] );
        System.out.println("公钥>>>>>>>>" + publicKeyString);
        // 得到私钥字符串
        String privateKeyString = Base64.encode( privateKey != null ? privateKey.getEncoded() : new byte[0] );
        System.out.println("秘钥" + privateKeyString);
    }
}
