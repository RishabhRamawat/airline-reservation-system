package com.airline.reservation.config;

import com.inngest.Inngest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the Inngest event client.
 */
@Configuration
public class InngestConfig {

    @Bean
    public Inngest inngestClient(@Value("${inngest.app-id}") String appId) {
        return new Inngest(appId);
    }
}
