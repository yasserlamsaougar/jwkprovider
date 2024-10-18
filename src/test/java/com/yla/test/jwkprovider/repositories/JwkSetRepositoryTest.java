package com.yla.test.jwkprovider.repositories;

import com.yla.test.jwkprovider.models.JwkModel;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import io.jsondb.JsonDBTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwkSetRepositoryTest {

    @Mock
    private JsonDBTemplate template;

    @InjectMocks
    private JwkSetRepository jwkSetRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getJwkSet_shouldReturnJwkList() {
        when(template.findAll(JwkModel.class)).thenReturn(Collections.emptyList());

        List<JwkModel> jwks = jwkSetRepository.getJwkSet();

        assertNotNull(jwks);
        assertTrue(jwks.isEmpty());
        verify(template, times(1)).findAll(JwkModel.class);
    }

    @Test
    void storeJwkSet_shouldStoreJwk() throws Exception {
        var mockJwk = new RSAKeyGenerator(2048).keyID(UUID.randomUUID().toString()).generate();
        jwkSetRepository.storeJwkSet(mockJwk);

        verify(template, times(1)).insert(any(JwkModel.class));
    }

    @Test
    void getJwkSetByKey_shouldReturnJwkModel() {
        var keyId = UUID.randomUUID().toString();
        var mockJwkModel = new JwkModel(keyId, Collections.emptyMap());
        when(template.findById(keyId, JwkModel.class)).thenReturn(mockJwkModel);

        JwkModel jwkModel = jwkSetRepository.getJwkSetByKey(keyId);

        assertNotNull(jwkModel);
        assertEquals(keyId, jwkModel.getKid());
        verify(template, times(1)).findById(keyId, JwkModel.class);
    }
}
