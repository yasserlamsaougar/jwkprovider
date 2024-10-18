package com.yla.test.jwkprovider.models;


import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Document(collection = "jwks", schemaVersion = "1.0")
@AllArgsConstructor
@Getter
@Setter
public class JwkModel {

    @Id
    private String kid;

    private Map<String, Object> jwk;

}
