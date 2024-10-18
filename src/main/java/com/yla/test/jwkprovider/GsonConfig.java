package com.yla.test.jwkprovider;

import com.nimbusds.jose.shaded.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GsonConfig {


    @Bean
    public Gson gson() {
        return new Gson();
    }
}
