package com.example.dependencymonitor.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.boot.actuate.health.Status;
import com.example.dependencymonitor.health.H2HealthIndicator;
import com.example.dependencymonitor.health.ExternalServiceHealthIndicator;

@Component
public class CustomMetricsCollector {

    private final MeterRegistry meterRegistry;
    private final H2HealthIndicator h2HealthIndicator;
    private final ExternalServiceHealthIndicator externalServiceHealthIndicator;

    public CustomMetricsCollector(MeterRegistry meterRegistry,
                                  H2HealthIndicator h2HealthIndicator,
                                  ExternalServiceHealthIndicator externalServiceHealthIndicator) {
        this.meterRegistry = meterRegistry;
        this.h2HealthIndicator = h2HealthIndicator;
        this.externalServiceHealthIndicator = externalServiceHealthIndicator;
        initializeGauges();
    }

    private void initializeGauges() {
        Gauge.builder("dependency.health.h2", this, value -> checkH2Health() ? 1 : 0)
                .description("H2 Database connectivity status")
                .register(meterRegistry);

        Gauge.builder("dependency.health.externalservice", this, value -> checkExternalServiceHealth() ? 1 : 0)
                .description("External Service connectivity status")
                .register(meterRegistry);
    }

    private boolean checkH2Health() {
        return h2HealthIndicator.health().getStatus() == Status.UP;
    }

    private boolean checkExternalServiceHealth() {
        return externalServiceHealthIndicator.health().getStatus() == Status.UP;
    }
}