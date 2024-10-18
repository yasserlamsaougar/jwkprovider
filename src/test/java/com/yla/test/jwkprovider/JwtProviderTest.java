package com.yla.test.jwkprovider;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.yla.test.jwkprovider.jwkprovider.JwkProvider;
import com.yla.test.jwkprovider.jwkprovider.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtProviderTest {

    @Mock
    private JwkProvider jwkProvider;

    @InjectMocks
    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateJWTToken_shouldGenerateToken() throws ParseException, JOSEException {
        var keyId = UUID.randomUUID().toString();
        var mockJwk = new RSAKeyGenerator(2048).keyID(keyId).generate();
        when(jwkProvider.getJwk(keyId)).thenReturn(mockJwk);

        JWT jwt = jwtProvider.generateJWTToken(keyId, Duration.ofMinutes(30), Map.of("user", "test"));

        assertNotNull(jwt);
        assertEquals("RS256", jwt.getHeader().getAlgorithm().getName());
        assertEquals("test", ((JWTClaimsSet) jwt.getJWTClaimsSet()).getClaim("user"));
        verify(jwkProvider, times(1)).getJwk(keyId);
    }
}
