package com.yla.test.jwkprovider;


import com.yla.test.jwkprovider.jwkprovider.JwkProvider;
import com.yla.test.jwkprovider.jwkprovider.JwtProvider;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.Duration;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JwkController {

    private final JwkProvider jwkProvider;
    private final JwtProvider jwtProvider;
    private final Gson gson;

    public record GenerateJWTRequest(Duration duration, Map<String, Object> claims){}
    public record GenerateJWTResponse(String base64, Map<String, Object> claimSet){}
    @PostMapping(value = "/jwt/{kid}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<String> generateJWT(@PathVariable String kid, @RequestBody GenerateJWTRequest request) {
        try {
            var generateJWT = jwtProvider.generateJWTToken(kid, request.duration, request.claims);
            var result = gson.toJson(new GenerateJWTResponse(generateJWT.serialize(), generateJWT.getJWTClaimsSet().toJSONObject()));
            return ResponseEntity.ok(result);
        } catch (ParseException | JOSEException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(
            path = "/jwkset",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<String> getJwkSet() {
        return ResponseEntity.ofNullable(jwkProvider.getJwkSet().toString(true));
    }


    @PostMapping(
            value = "/jwkset",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<String> generateJWK() {
        try {
            var jwk = jwkProvider.generateJwk();
            return ResponseEntity.ofNullable(jwk.toPublicJWK().toJSONString());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
