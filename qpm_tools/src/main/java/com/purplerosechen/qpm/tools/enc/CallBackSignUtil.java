package com.purplerosechen.qpm.tools.enc;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Random;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 回调签名加解密帮助类
 * @date 14 4月 2025 14:14
 */

@Slf4j
public class CallBackSignUtil {

    private static final int ED25519_SEED_SIZE = 32;

    /** 
     * @description: TODO 验证签名是否对应 
     * @author chen
     * @date: 15 4月 2025 14:03
     */ 
    public static boolean verifySignature(String appSecret, String xSignatureEd25519, String xSignatureTimestamp, String reqBody) throws IOException {
        byte[] seed = expandSeed(appSecret.getBytes(StandardCharsets.UTF_8));

        // 用 seed 构造 Ed25519 私钥
        Ed25519PrivateKeyParameters privateKey = new Ed25519PrivateKeyParameters(seed, 0);

        // 从私钥推导出公钥
        Ed25519PublicKeyParameters publicKey = privateKey.generatePublicKey();
        byte[] signature = hexStringToByteArray(xSignatureEd25519);

        if (signature.length != 64 || (signature[63] & 0xE0) != 0) {
            return false;
        }

        ByteArrayOutputStream msg = new ByteArrayOutputStream();
        msg.write(xSignatureTimestamp.getBytes());
        msg.write(reqBody.getBytes());
        byte[] msgBytes = msg.toByteArray();

        Ed25519Signer signer = new Ed25519Signer();
        signer.init(false, publicKey);
        signer.update(msgBytes, 0, msgBytes.length);
        return signer.verifySignature(signature);
    }

    /**
     * @description: TODO  生成秘钥
     * @author chen
     * @date: 15 4月 2025 13:50
     */
    public static String generateResponse(String botSecret, String eventTs, String plainToken) throws Exception {
        byte[] seed = expandSeed(botSecret.getBytes(StandardCharsets.UTF_8));
        Ed25519PrivateKeyParameters privateKey = new Ed25519PrivateKeyParameters(seed, 0);

        // 生成Ed25519密钥对
        ByteArrayOutputStream msg = new ByteArrayOutputStream();
        msg.write(eventTs.getBytes());
        msg.write(plainToken.getBytes());
        byte[] msgBytes = msg.toByteArray();

        Ed25519Signer signer = new Ed25519Signer();
        signer.init(true, privateKey);
        signer.update(msgBytes, 0, msgBytes.length);
        byte[] signature = signer.generateSignature();

        return bytesToHex(signature);
    }

    /** 
     * @description: TODO 字节转换
     * @author chen
     * @date: 15 4月 2025 13:49
     */ 
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    /**
     * @description: TODO 秘钥补齐
     * @author chen
     * @date: 15 4月 2025 13:50
     */
    private static byte[] expandSeed(byte[] input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while (output.size() < ED25519_SEED_SIZE) {
            output.writeBytes(input);
        }
        return Arrays.copyOf(output.toByteArray(), ED25519_SEED_SIZE);
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        if ((len & 1) != 0) {
            throw new IllegalArgumentException("Hex string must have even length");
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) (
                    (Character.digit(s.charAt(i), 16) << 4)
                            + Character.digit(s.charAt(i + 1), 16)
            );
        }
        return data;
    }
}
