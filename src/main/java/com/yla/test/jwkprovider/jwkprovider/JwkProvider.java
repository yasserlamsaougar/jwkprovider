package com.yla.test.jwkprovider.jwkprovider;

import com.yla.test.jwkprovider.repositories.JwkSetRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwkProvider {

    private final JwkSetRepository jwkSetRepository;

    public JWKSet getJwkSet() {
        var jwkModels = jwkSetRepository.getJwkSet();
        var jwks = jwkModels.stream().map(model -> {
            try {
                return JWK.parse(model.getJwk());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        return new JWKSet(jwks);
    }

    public JWK getJwk(String kid) throws ParseException {
        var jwkModel = this.jwkSetRepository.getJwkSetByKey(kid);
        return JWK.parse(jwkModel.getJwk());
    }

    public JWK generateJwk() throws JOSEException {
        var jwk = new RSAKeyGenerator(2048)
                .keyUse(KeyUse.SIGNATURE) // indicate the intended use of the key (optional)
                .keyID(UUID.randomUUID().toString()) // give the key a unique ID (optional)
                .issueTime(new Date()) // issued-at timestamp (optional)
                .generate();
        this.jwkSetRepository.storeJwkSet(jwk);
        return jwk;
    }



}
