package com.mr.test;

import com.baidu.shop.dto.UserInfo;
import com.baidu.shop.utils.JwtUtils;
import com.baidu.shop.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/15 14:24
 */
public class JwtTokenTest {

    //公钥位置
    private static final String pubkeyPath = "E:\\secret\rea.pub";
    //私钥位置
    private static final String prikeyPath = "E:\\secret\rea.pri";
    //公钥对象
    private PublicKey publicKey;
    //私钥对象
    private PrivateKey privateKey;


    /**
     *  根据密文生成公钥私钥
     * @throws Exception
     */
    @Test
    public void genResKey() throws Exception {
        RsaUtils.generateKey(pubkeyPath,prikeyPath,"mingrui");
    }

    /**
     * 从文件中读取公钥密钥
     * @throws Exception
     */
    @Before
    public void getKeyByRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubkeyPath);
        this.privateKey = RsaUtils.getPrivateKey(prikeyPath);
    }

    /**
     * 根据用户信息结合私钥生成token
     * @throws Exception
     */
    @Test
    public void genToken() throws Exception {
        //生成token
        String token = JwtUtils.generateToken(new UserInfo(1, "wsq"), privateKey, 2);
        System.out.println("user-token = " + token );
    }

    /**
     * 结合公钥解析token
     * @throws Exception
     */
    @Test
    public void parseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJ3c3EiLCJleHAiOjE2MDI3NDQ3MDF9.KxLv9nxq2QvKj2zspA4hAGqQ0Ogkb9n-fTp050_Bflp-sZbVqk-t53J-SkP7YIeuucNBmScE3dfFwQCD6fu-wDvPMNXgz1BPOPAAjqVSKEtbuZpbpraVGVGj4xFox9dibjqfJv0RKA-98D6ZvZMdESKcnQVLB0w8okMyKTq8JoY";
        //解析token
        UserInfo userInfo = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + userInfo.getId());
        System.out.println("userName: " + userInfo.getUsername());

    }
}
