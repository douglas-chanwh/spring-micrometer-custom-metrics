
package com.example.dependencymonitor.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class H2HealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    public H2HealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Health health() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1 FROM DUAL", Integer.class);
            if (result != null && result == 1) {
                return Health.up().withDetail("database", "H2").build();
            } else {
                return Health.down().withDetail("database", "H2").withDetail("error", "Unexpected query result").build();
            }
        } catch (Exception e) {
            return Health.down().withDetail("database", "H2").withDetail("error", e.getMessage()).build();
        }
    }
}