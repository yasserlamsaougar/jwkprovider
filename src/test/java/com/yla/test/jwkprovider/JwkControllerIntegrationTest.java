package com.yla.test.jwkprovider;

import com.yla.test.jwkprovider.jwkprovider.JwkProvider;
import com.yla.test.jwkprovider.jwkprovider.JwtProvider;
import com.yla.test.jwkprovider.repositories.JwkSetRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.shaded.gson.Gson;
import io.jsondb.JsonDBTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class JwkControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwkProvider jwkProvider;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private Gson gson;

    @MockBean
    private JsonDBTemplate jsonDBTemplate;

    @MockBean
    private JwkSetRepository jwkSetRepository;

    private RSAKey mockRsaKey;

    @BeforeEach
    void setUp() throws JOSEException {
        Mockito.reset(jwkProvider, jwtProvider, gson, jsonDBTemplate, jwkSetRepository);
        mockRsaKey = new RSAKeyGenerator(2048)
                .keyID(UUID.randomUUID().toString())
                .generate();
    }
    @Test
    void getJwkSet_shouldReturnJwkSet() throws Exception {
        var jwkSetJson = "{\"keys\": [{\"kty\":\"RSA\",\"kid\":\"" + mockRsaKey.getKeyID() + "\"}]}";

        when(jwkProvider.getJwkSet()).thenReturn(new com.nimbusds.jose.jwk.JWKSet(mockRsaKey));
        when(gson.toJson(any())).thenReturn(jwkSetJson);

        mockMvc.perform(MockMvcRequestBuilders.get("/jwkset")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void generateJWK_shouldReturnGeneratedJwk() throws Exception {
        when(jwkProvider.generateJwk()).thenReturn(mockRsaKey);
        when(jwkProvider.getJwkSet()).thenReturn(new com.nimbusds.jose.jwk.JWKSet(mockRsaKey));

        mockMvc.perform(MockMvcRequestBuilders.post("/jwkset")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.kty").value("RSA"))
                .andExpect(jsonPath("$.kid").value(mockRsaKey.getKeyID()));
    }
}
