package com.yla.test.jwkprovider.repositories;


import com.yla.test.jwkprovider.models.JwkModel;
import io.jsondb.JsonDBTemplate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class AsyncLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncLoader.class);

    private final JsonDBTemplate jsonDBTemplate;

    private <T> void createCollection(Class<T> documentAnnotatedClass) {
        if(!jsonDBTemplate.collectionExists(documentAnnotatedClass)) {
            LOGGER.info("Creating collection {}", documentAnnotatedClass.getSimpleName());
            jsonDBTemplate.createCollection(documentAnnotatedClass);
            LOGGER.info("Created collection {}", documentAnnotatedClass.getSimpleName());
        }
    }

    public void createCollections() {
        Stream.of(JwkModel.class).forEach(this::createCollection);
    }

}
