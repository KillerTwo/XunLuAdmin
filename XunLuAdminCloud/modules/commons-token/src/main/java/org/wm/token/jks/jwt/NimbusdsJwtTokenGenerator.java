package org.wm.token.jks.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.wm.commons.constants.SecurityConstants;
import org.wm.commons.exception.ServiceException;
import org.wm.token.jks.RSAGenerator;
import org.wm.commons.utils.StringUtils;
import org.wm.commons.utils.uuid.IdUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;

/**
 * 功能描述：<功能描述>
 * jwt token Nimbusds实现
 *
 * @author dove
 * @date 2023/07/15 19:50
 * @since 1.0
 **/
@Slf4j
@RequiredArgsConstructor
@Component
public class NimbusdsJwtTokenGenerator implements TokenGenerator<JWTClaimsSet> {

    protected static final long MILLIS_SECOND = 1000;

    private final RSAGenerator rsaGenerator;


    public static void main(String[] args) {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .jwtID(IdUtils.fastSimpleUUID())
                .expirationTime(new Date(MILLIS_SECOND * 1000))
                // .claim(SecurityConstants.DETAILS_USER_ID, claims.get(SecurityConstants.DETAILS_USER_ID))
                // .claim(SecurityConstants.DETAILS_USERNAME, claims.get(SecurityConstants.DETAILS_USERNAME))
                // .claim(SecurityConstants.USER_KEY, claims.get(SecurityConstants.USER_KEY))

                .build();

        claimsSet.getClaims().putAll(Map.of("key1", "v1", "key2", "v2"));

    }


    @Override
    public String createToken(Map<String, Object> claims) {

        var second = LocalDateTime.now().plusMinutes(30).toEpochSecond(ZoneOffset.UTC);


        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .jwtID(IdUtils.fastSimpleUUID())
                .expirationTime(new Date(MILLIS_SECOND * second))
                .subject((String) claims.get(SecurityConstants.DETAILS_USERNAME))
                .claim(SecurityConstants.DETAILS_USER_ID, claims.get(SecurityConstants.DETAILS_USER_ID))
                .claim(SecurityConstants.DETAILS_USERNAME, claims.get(SecurityConstants.DETAILS_USERNAME))
                .build();

        return createToken(claimsSet);
    }

    @Override
    public String createToken(JWTClaimsSet claims) {
        var rsaJWK = rsaGenerator.getRsaKey();
        try {
            JWSSigner signer = new RSASSASigner(rsaJWK);


            JWSHeader jwsHeader = new JWSHeader
                    .Builder(JWSAlgorithm.RS256)    // 指定 RSA 算法
                    .type(JOSEObjectType.JWT)
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    jwsHeader,
                    claims);

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public JWTClaimsSet parseToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        log.info("token {} ", token);
        try {
            var decodeJWT = SignedJWT.parse(token);

            var rsaJWK = rsaGenerator.getRsaKey();
            var rsaPublicJWK = rsaJWK.toPublicJWK();

            JWSVerifier verifier = new RSASSAVerifier(rsaPublicJWK);
            if (decodeJWT.verify(verifier)) {
                log.info("{}", decodeJWT.getJWTClaimsSet());
                return decodeJWT.getJWTClaimsSet();
            }
            return null;
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object getValue(JWTClaimsSet claims, String key) {
        return claims.getClaim(key);
    }
}
