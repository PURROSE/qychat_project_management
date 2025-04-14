package com.purplerosechen.qpm.tools.enc;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 回调签名加解密帮助类
 * @date 14 4月 2025 14:14
 */

@Slf4j
public class CallBackSignUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static boolean verifySignature(String botSecret, String xSignatureEd25519, String timeReqBody) throws Exception {
        String seed = generateSeed(botSecret);
        byte[] sig = Hex.decodeHex(xSignatureEd25519);

        if (sig.length != 64) {
            return false;
        }
        // 校验签名的最后一位是否符合特定的位掩码条件
        if ((sig[63] & 0xE0) != 0) {
            return false;
        }

        log.warn("seed:{}" , seed);

        Signature signature = Signature.getInstance("Ed25519", "BC");
        signature.initVerify(generateEd25519KeyPair(seed.getBytes()).getPublic());
        signature.update(timeReqBody.getBytes(StandardCharsets.UTF_8));
        return signature.verify(sig);
    }

    public static String generateResponse(String botSecret, String eventTs, String plainToken) throws Exception {
        // 生成seed
        String seed = generateSeed(botSecret);

        // 生成Ed25519密钥对
        KeyPair keyPair = generateEd25519KeyPair(seed.getBytes());

        // 组合消息
        byte[] msg = (eventTs + plainToken).getBytes();

        // 生成签名
        byte[] signature = signEd25519(keyPair.getPrivate(), msg);

        return Hex.encodeHexString(signature);
    }

    /** 
     * @description: TODO 生成秘钥字符串 
     * @author chen
     * @date: 14 4月 2025 15:35
     */ 
    private static String generateSeed(String botSecret) {
        StringBuilder seedBuilder = new StringBuilder(botSecret);
        // 重复 Bot Secret 直到达到或超过 32 字节
        while (seedBuilder.length() < 32) {
            seedBuilder.append(seedBuilder);
        }
        // 如果超过 32 字节，截取前 32 字节
        return seedBuilder.substring(0, 32);
    }

    /** 
     * @description: TODO 生成秘钥对 
     * @author chen
     * @date: 14 4月 2025 15:35
     */ 
    private static KeyPair generateEd25519KeyPair(byte[] seed) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("Ed25519", "BC");
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(seed);
        keyPairGenerator.initialize(255, secureRandom);
        return keyPairGenerator.generateKeyPair();
    }

    /** 
     * @description: TODO 生成签名 
     * @author chen
     * @date: 14 4月 2025 15:35
     */ 
    private static byte[] signEd25519(PrivateKey privateKey, byte[] msg) throws Exception {
        Signature signature = Signature.getInstance("Ed25519", "BC");
        signature.initSign(privateKey);
        signature.update(msg);
        return signature.sign();
    }
}
