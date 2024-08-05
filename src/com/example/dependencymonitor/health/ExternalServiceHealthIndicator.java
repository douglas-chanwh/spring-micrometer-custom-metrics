
package com.example.dependencymonitor.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Component
public class ExternalServiceHealthIndicator implements HealthIndicator {

    private final RestTemplate restTemplate;
    private final String externalServiceUrl;

    public ExternalServiceHealthIndicator(RestTemplate restTemplate,
                                          @Value("${external.service.url}") String externalServiceUrl) {
        this.restTemplate = restTemplate;
        this.externalServiceUrl = externalServiceUrl;
    }

    @Override
    public Health health() {
        try {
            restTemplate.getForObject(externalServiceUrl, String.class);
            return Health.up().withDetail("service", "External API").build();
        } catch (Exception e) {
            return Health.down().withDetail("service", "External API").withDetail("error", e.getMessage()).build();
        }
    }
}
