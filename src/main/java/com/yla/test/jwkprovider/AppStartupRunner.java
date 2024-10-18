package com.yla.test.jwkprovider;

import com.yla.test.jwkprovider.repositories.AsyncLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppStartupRunner implements ApplicationRunner {

    @Value("${phenix.ldap.stub.api.init.enabled:true}")
    private boolean stubApiInitEnabled;

    private final AsyncLoader asyncLoader;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Starting application...");
        if(stubApiInitEnabled) {
            asyncLoader.createCollections();
        }
    }
}
