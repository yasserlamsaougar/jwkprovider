package com.yla.test.jwkprovider.repositories;

import com.yla.test.jwkprovider.models.JwkModel;
import com.nimbusds.jose.jwk.JWK;
import io.jsondb.JsonDBTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwkSetRepository {

    private final JsonDBTemplate template;

    public List<JwkModel> getJwkSet() {
        return template.findAll(JwkModel.class);
    }

    public void storeJwkSet(JWK jwk) {
        var kid = jwk.getKeyID();
        template.insert(new JwkModel(kid, jwk.toJSONObject()));
    }

    public JwkModel getJwkSetByKey(String key) {
        return template.findById(key, JwkModel.class);
    }

}
