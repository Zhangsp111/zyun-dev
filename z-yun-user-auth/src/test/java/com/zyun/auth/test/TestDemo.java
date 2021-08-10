package com.zyun.auth.test;

import com.alibaba.fastjson.JSON;
import com.zyun.auth.client.UserControllerClient;
import com.zyun.model.system.user.ext.UserExt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TestDemo
 * @Author: zsp
 * @Date 2021/3/16 12:24
 * @Description:
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDemo {

    @Autowired
    private UserControllerClient controllerClient;

    @Test
    public void testClient() {
        UserExt admin = controllerClient.getUserByUsername("admin");
        System.out.println(admin);
    }


    /**
     * 测试生成jwt令牌
     */
    @Test
    public void testGetToken() {
        // 证书文件名称
        String keyLocation = "zyun.keystore";
        // 定义密钥库访问密码
        String storepass = "zyunkeystore";
        ClassPathResource classPathResource = new ClassPathResource(keyLocation);
        // 创建keystore工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, storepass.toCharArray());

        // 定义密钥访问密码
        String keypass = "zyunpass";
        // 定义密钥别名
        String alias = "zyunkey";
        // 别名和密码要匹配
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypass.toCharArray());
        // 获取私钥
        RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();

        //定义负载信息(JWT组成第二部分的JSON内容)
        Map<String,Object> map = new HashMap<>();
        map.put("id", "zy");
        map.put("name", "zy");
        String jsonString = JSON.toJSONString(map);
        // 获取密钥
        Jwt encode = JwtHelper.encode(jsonString, new RsaSigner(aPrivate));
        // 打印
        String encoded = encode.getEncoded();
        System.out.println(encoded);
    }

    /**
     * 校验令牌的正确性
     */
    @Test
    public void testCheck() {
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInJvbGVJZCI6MSwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiLkv53lronogIHlvKAiLCJpZCI6IjQ2IiwiZXhwIjoxNjE4MzE3NTY0LCJhdXRob3JpdGllcyI6WyJTWVNURU0iLCJTWVNURU06TUVOVSJdLCJqdGkiOiI1Njc4M2M2Mi0zYTM4LTRlODEtYTQwNy04YWU3MzcwNjAzY2UiLCJjbGllbnRfaWQiOiJaWXVuV2ViQXBwIn0.qFezYwACs_xXXTcBQC7Zo0gWq7jgKkgP21TwXt6Ih_a__F8BcJDDNgUDYkGz0bzIVICdBxMQUc4Nsv7ZzbeVxRxpfU_GJlZ7wdPX7Cyv4PxP6LDSj99lgvAiR6xWyLeLbMyvR0sk5zhBHxvURk9-GbZxfUn6pa-HKQvoLrU58ChiobbZr4XyE9-OXQkLyL_7Bb8FJHQvwsAMW1bzaJxoxvxjydit6xHnOUIahq_5Hpks17410uU9BQXivskw7g2PYmg8gBuQmuzM6GObvwcmJ37bn23bCQdw9g70UFZbIaE643uxsBXRjS8Od6MltK7nO7fm-IlszhIuv4krBpykcg";
        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AM" +
                "IIBCgKCAQEAvQZh/vL0mI0TWw6GI5V+8iMjzDsiVEOyhsTIx8KWxPxsE9fnMKl9t9/Zimhnkl" +
                "ESbVInbIpezLivA1f0uDoBfasPnQlv0fQ9XBnJu/kCpM8ng9XN+YRGAWTQG7gWFRogIhsHF6emWm" +
                "EQkUsR7S94BuWHgwWiJnna+GP8yuYLbDjf7+2K8Fbt04JasQaSVL1d6SO/pmbtBaUgQTpm5TOTNPo" +
                "dEi0VDRo+u8tPV1WQ+0yoUosoR1zBoX1ZzFpXNHV+Radtb9e5/SPodfVtrGafGYiJIOrmm+hnEWeAuaZmOZu" +
                "22o3PaAUGzzti6xHzQYZbVvAAYcApDy/SgFpuJqQmdQIDAQAB-----END PUBLIC KEY-----";

        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));

        // 获取负载
        String claims = jwt.getClaims();

        System.out.println(claims);
    }

}
