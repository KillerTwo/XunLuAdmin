package org.wm.generator.utils;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 功能描述：RSA加解密工具类
 * @author dove 
 * @date 2025/08/10 18:07
 * @since 1.0
**/
public class RSAUtils {

    private static final String ALGORITHM = "RSA";

    public static byte[] encryptByPublicKey(String publicKey, byte[] content) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM); // 明确指定填充方案
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKeyFromBase64(publicKey));
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decryptByPrivateKey(String privateKey, byte[] content) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM); // 与加密使用相同的填充方案
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKeyFromBase64(privateKey));
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptByPublicKey(PublicKey publicKey, byte[] content) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM); // 明确指定填充方案
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decryptByPrivateKey(PrivateKey privateKey, byte[] content) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM); // 与加密使用相同的填充方案
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptByPrivateKey(String privateKey, byte[] content) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(getPrivateKeyFromBase64(privateKey));
            signature.update(content);
            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean decryptByPublicKey(String publicKey, byte[] content, byte[] signature) {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(getPublicKeyFromBase64(publicKey));
            sig.update(content);
            return sig.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static RSAKeyInfo  getRSAKeyInfo() {

        try {
            // 创建RSA密钥对生成器
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);

            // 初始化密钥长度，推荐至少2048位
            keyPairGenerator.initialize(2048);

            // 生成密钥对
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // 获取公钥和私钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            // 打印密钥信息
            var publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
           /* System.out.println("公钥(Base64编码):");
            System.out.println(publicKeyStr);*/

            var privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            /*System.out.println("\n私钥(Base64编码):");
            System.out.println(privateKeyStr);*/

            return new RSAKeyInfo(publicKeyStr, privateKeyStr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static RSAPublicKey getPublicKeyFromBase64(String base64PublicKey) throws Exception {
        // 1. 解码Base64字符串
        byte[] keyBytes = Base64.getDecoder().decode(base64PublicKey);

        // 2. 创建X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // 3. 使用KeyFactory生成公钥
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    public static RSAPrivateKey getPrivateKeyFromBase64(String base64PrivateKey) throws Exception {
        // 1. 解码Base64字符串
        byte[] keyBytes = Base64.getDecoder().decode(base64PrivateKey);

        // 2. 创建PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        // 3. 使用KeyFactory生成私钥
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    // 将公钥转换为PEM格式
    public static String convertPublicKeyToPEM(PublicKey publicKey) {
        byte[] encoded = publicKey.getEncoded();
        String base64 = Base64.getEncoder().encodeToString(encoded);

        return "-----BEGIN PUBLIC KEY-----\n" +
                formatPEM(base64) +
                "-----END PUBLIC KEY-----";
    }

    // 将私钥转换为PEM格式
    public static String convertPrivateKeyToPEM(PrivateKey privateKey) {
        byte[] encoded = privateKey.getEncoded();
        String base64 = Base64.getEncoder().encodeToString(encoded);

        return "-----BEGIN PRIVATE KEY-----\n" +
                formatPEM(base64) +
                "-----END PRIVATE KEY-----";
    }

    // 格式化PEM字符串，每64个字符换行
    private static String formatPEM(String base64) {
        StringBuilder builder = new StringBuilder();
        int length = base64.length();

        for (int i = 0; i < length; i += 64) {
            int end = Math.min(i + 64, length);
            builder.append(base64, i, end);
            builder.append("\n");
        }

        return builder.toString();
    }

    public static String convertToPEM(Object key, String description) throws IOException {
        StringWriter writer = new StringWriter();
        PemObject pemObject = new PemObject(description, ((Key)key).getEncoded());
        PemWriter pemWriter = new PemWriter(writer);
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        return writer.toString();
    }

    public static String privateKeyConvertToPEM(PrivateKey key) throws IOException {
        return convertToPEM(key, "PRIVATE KEY");
    }

    public static String publicKeyConvertToPEM(PublicKey key) throws IOException {
        return convertToPEM(key, "PUBLIC KEY");
    }

    public static RSAPublicKey convertPemToPublicKey(String pemPublicKey) throws IOException {
        // 移除PEM格式的头部和尾部
        String publicKeyPEM = pemPublicKey
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", ""); // 移除所有空白字符

        // 使用PEMParser读取PEM内容
        try (PEMParser pemParser = new PEMParser(new StringReader(pemPublicKey))) {
            Object object = pemParser.readObject();
            if (object instanceof SubjectPublicKeyInfo publicKeyInfo) {
                PublicKey publicKey = new JcaPEMKeyConverter().getPublicKey(publicKeyInfo);
                if (publicKey instanceof RSAPublicKey) {
                    return (RSAPublicKey) publicKey;
                }
            }
            throw new IllegalArgumentException("提供的PEM不是有效的RSA公钥");
        }
    }

    public static RSAPrivateKey convertPemToPrivateKey(String pemPrivateKey) throws IOException {
        // 使用PEMParser读取PEM内容
        try (PEMParser pemParser = new PEMParser(new StringReader(pemPrivateKey))) {
            Object object = pemParser.readObject();

            if (object instanceof PrivateKeyInfo privateKeyInfo) {
                // PKCS#8格式
                PrivateKey privateKey = new JcaPEMKeyConverter().getPrivateKey(privateKeyInfo);
                if (privateKey instanceof RSAPrivateKey) {
                    return (RSAPrivateKey) privateKey;
                }
            } else if (object instanceof org.bouncycastle.asn1.pkcs.RSAPrivateKey) {
                // PKCS#1格式
                org.bouncycastle.asn1.pkcs.RSAPrivateKey rsaPrivateKey =
                        (org.bouncycastle.asn1.pkcs.RSAPrivateKey) object;
                PrivateKey privateKey = new JcaPEMKeyConverter().getPrivateKey(
                        PrivateKeyInfo.getInstance(rsaPrivateKey)
                );
                if (privateKey instanceof RSAPrivateKey) {
                    return (RSAPrivateKey) privateKey;
                }
            }

            throw new IllegalArgumentException("提供的PEM不是有效的RSA私钥");
        }
    }

    public static String readPemFromFile(String pemPath) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(Paths.get(pemPath))) {
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    record RSAKeyInfo(String publicKey, String privateKey) {
    }


    public static void main(String[] args) throws Exception {
        /*var info = getRSAKeyInfo();

        System.err.println("\n私钥(Base64编码):");
        System.err.println(info.privateKey());
        System.err.println("私钥PEM:");
        System.err.println(privateKeyConvertToPEM(getPrivateKeyFromBase64(info.privateKey)));


        System.err.println("\n公钥(Base64编码):");
        System.err.println(info.publicKey());
        System.err.println("公钥PEM:");
        System.err.println(publicKeyConvertToPEM(getPublicKeyFromBase64(info.publicKey)));*/


        System.err.println("\n-------------------");
        var s = "hello world";

        var publicKey = readPemFromFile("/Users/dove/code/GithubProject/Java/source/XunLuGenerator/xunlu-generator-server/src/main/resources/rsa/pub.pem");

        var encrypt= encryptByPublicKey(convertPemToPublicKey(publicKey),  s.getBytes());
        var enc = Base64.getEncoder().encodeToString(encrypt);
        System.err.println(enc);

        var privateKey = readPemFromFile("/Users/dove/code/GithubProject/Java/source/XunLuGenerator/xunlu-generator-server/src/main/resources/rsa/privateKey.pem");
        var decrypt = decryptByPrivateKey(convertPemToPrivateKey(privateKey),  Base64.getDecoder().decode(enc));
        System.err.println(new String(decrypt));
    }


}
