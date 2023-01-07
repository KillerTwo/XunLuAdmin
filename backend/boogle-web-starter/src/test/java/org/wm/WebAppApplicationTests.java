package org.wm;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wm.jks.RSAGenerator;
import org.wm.service.ISysDictDataService;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@SpringBootTest
class WebAppApplicationTests {

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Autowired
    private RSAGenerator rsaGenerator;

    @Test
    void contextLoads() {
    }


    @Test
    public void testTrasaction() throws Exception {
        sysDictDataService.testMain();
    }

    @Test
    public void testGenerateJWT() throws JOSEException, ParseException {

        var rsaJWK = rsaGenerator.getRsaKey();

        JWSSigner signer = new RSASSASigner(rsaJWK);


        var second = LocalDateTime.now().plusMinutes(30).toEpochSecond(ZoneOffset.UTC);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("alice")
                .expirationTime(new Date(second * 1000))
                .claim("userId", "123445")
                .build();

        JWSHeader jwsHeader = new JWSHeader
                .Builder(JWSAlgorithm.RS256)    // 指定 RSA 算法
                .type(JOSEObjectType.JWT)
                .build();

        SignedJWT signedJWT = new SignedJWT(
                jwsHeader,
                claimsSet);

        signedJWT.sign(signer);

        String token = signedJWT.serialize();

        System.err.println(token);
        System.err.println("-----------------");

        var decodeJWT = SignedJWT.parse(token);

        var rsaPublicJWK = rsaJWK.toPublicJWK();

        JWSVerifier verifier = new RSASSAVerifier(rsaPublicJWK);
        if (decodeJWT.verify(verifier)) {
            System.err.println(decodeJWT.getJWTClaimsSet());
        }
    }

}
