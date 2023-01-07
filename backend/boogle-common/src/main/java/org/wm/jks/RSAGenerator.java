package org.wm.jks;

import com.nimbusds.jose.jwk.RSAKey;
import lombok.Data;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.wm.exception.ServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Data
public class RSAGenerator {

    private RSAKey rsaKey;

    private RSAGenerator() {

    }

    public static RSAGenerator getInstance(String path, String aliasName, String password) {
        var in = loadCer(path);
        char[] pwd  = password.toCharArray();
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(in, pwd);
            Key key = keyStore.getKey(aliasName, pwd);
            KeyPair keyPair = null;
            if (key instanceof PrivateKey) {
                Certificate certificate = keyStore.getCertificate(aliasName);
                PublicKey publicKey = certificate.getPublicKey();
                keyPair = new KeyPair(publicKey, (PrivateKey)key);
            }

            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            var rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();
            var instance = new RSAGenerator();
            instance.setRsaKey(rsaKey);
            return instance;
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        } catch (CertificateException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        } catch (KeyStoreException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    private static InputStream loadCer(String path) {
        PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        var resource = resourceResolver.getResource(path);
        if (resource == null) {
            throw new ServiceException("加载资源失败");
        }
        try {
            var in = resource.getInputStream();
            return in;
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }


}
