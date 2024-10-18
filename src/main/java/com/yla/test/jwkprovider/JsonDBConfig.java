package com.yla.test.jwkprovider;


import io.jsondb.JsonDBTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonDBConfig {

    @Value("${jwk.stub.jsondb.filepath}")
    private String filePath;

    @Value("${jwk.stub.jsondb.packageToScan}")
    private String packageToScan;

    @Bean
    public JsonDBTemplate jsonDBTemplate() {
        return new JsonDBTemplate(filePath, packageToScan);

    }
}
