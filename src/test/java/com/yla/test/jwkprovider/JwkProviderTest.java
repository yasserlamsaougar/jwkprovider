package com.yla.test.jwkprovider;

import com.yla.test.jwkprovider.jwkprovider.JwkProvider;
import com.yla.test.jwkprovider.models.JwkModel;
import com.yla.test.jwkprovider.repositories.JwkSetRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwkProviderTest {

    @Mock
    private JwkSetRepository jwkSetRepository;

    @InjectMocks
    private JwkProvider jwkProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getJwkSet_shouldReturnJwkSet() throws ParseException, JOSEException {
        var mockJwk = new RSAKeyGenerator(2048)
                .keyID(UUID.randomUUID().toString())
                .generate();
        when(jwkSetRepository.getJwkSet()).thenReturn(Collections.singletonList(new JwkModel(mockJwk.getKeyID(), mockJwk.toJSONObject())));

        JWKSet jwkSet = jwkProvider.getJwkSet();

        assertNotNull(jwkSet);
        assertEquals(1, jwkSet.getKeys().size());
        verify(jwkSetRepository, times(1)).getJwkSet();
    }

    @Test
    void getJwk_shouldReturnJwk() throws ParseException, JOSEException {
        var keyId = UUID.randomUUID().toString();
        var mockJwk = new RSAKeyGenerator(2048).keyID(keyId).generate();
        when(jwkSetRepository.getJwkSetByKey(keyId)).thenReturn(new JwkModel(keyId, mockJwk.toJSONObject()));

        JWK jwk = jwkProvider.getJwk(keyId);

        assertNotNull(jwk);
        assertEquals(keyId, jwk.getKeyID());
        verify(jwkSetRepository, times(1)).getJwkSetByKey(keyId);
    }

    @Test
    void generateJwk_shouldGenerateAndStoreJwk() throws JOSEException {
        JWK jwk = jwkProvider.generateJwk();

        assertNotNull(jwk);
        assertEquals("sig", jwk.getKeyUse().identifier());
        verify(jwkSetRepository, times(1)).storeJwkSet(any(JWK.class));
    }
}
