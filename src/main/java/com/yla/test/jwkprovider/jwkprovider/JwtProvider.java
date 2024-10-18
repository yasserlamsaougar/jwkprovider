package com.yla.test.jwkprovider.jwkprovider;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwkProvider jwkProvider;

    public JWT generateJWTToken(String kid, Duration duration, Map<String, Object> claims) throws ParseException, JOSEException {
        var jwtClaims = new JWTClaimsSet.Builder()
                .expirationTime(new Date(System.currentTimeMillis() + duration.toMillis()));
        claims.forEach(jwtClaims::claim);
        var builtJwtClaims = jwtClaims.build();
        var jwk = jwkProvider.getJwk(kid);
        var publicJWK = jwk.toPublicJWK();
        var signer = new RSASSASigner(jwk.toRSAKey());
        var toBeSignedJWT =  new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).jwk(publicJWK).keyID(kid).build(),
                builtJwtClaims
        );
        toBeSignedJWT.sign(signer);
        return toBeSignedJWT;

    }

}
