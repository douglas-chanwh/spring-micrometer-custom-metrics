//<!-- pom.xml -->
/*<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.globalrelay</groupId>
    <artifactId>task-manager</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.6.3</version>
    </parent>

    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>

        <!-- Micrometer and Prometheus -->
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-core</artifactId>
        </dependency>
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
        </dependency>

        <!-- Kafka -->
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
        </dependency>

        <!-- CockroachDB JDBC Driver -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
        </dependency>


        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>


<!-- application.properties -->
        # Server port
server.port=8080

        # Actuator endpoints
management.endpoints.web.exposure.include=health,prometheus
management.endpoint.health.show-details=always

# Datasource configuration for CockroachDB
spring.datasource.url=jdbc:postgresql://localhost:26257/taskmanager
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=org.postgresql.Driver

# Kafka configuration
spring.kafka.bootstrap-servers=localhost:9092

        # Custom configuration for Service Manager
servicemanager.thrift.host=localhost
servicemanager.thrift.port=9090

<!-- CustomMetricsCollector.java -->
        import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.boot.actuate.health.Status;

@Component
public class CustomMetricsCollector {

    private final MeterRegistry meterRegistry;
    private final KafkaHealthIndicator kafkaHealthIndicator;
    private final CockroachDBHealthIndicator cockroachDBHealthIndicator;
    private final AAAHealthIndicator AAAHealthIndicator;

    public CustomMetricsCollector(MeterRegistry meterRegistry,
                                  KafkaHealthIndicator kafkaHealthIndicator,
                                  CockroachDBHealthIndicator cockroachDBHealthIndicator,
                                  AAAHealthIndicator AAAHealthIndicator) {
        this.meterRegistry = meterRegistry;
        this.kafkaHealthIndicator = kafkaHealthIndicator;
        this.cockroachDBHealthIndicator = cockroachDBHealthIndicator;
        this.AAAHealthIndicator = AAAHealthIndicator;
        initializeGauges();
    }

    private void initializeGauges() {
        Gauge.builder("dependency.health.kafka", this, value -> checkKafkaHealth() ? 1 : 0)
                .description("Kafka connectivity status")
                .register(meterRegistry);

        Gauge.builder("dependency.health.cockroachdb", this, value -> checkCockroachDBHealth() ? 1 : 0)
                .description("CockroachDB connectivity status")
                .register(meterRegistry);

        Gauge.builder("dependency.health.servicemanager", this, value -> checkServiceManagerHealth() ? 1 : 0)
                .description("Service Manager connectivity status")
                .register(meterRegistry);
    }

    private boolean checkKafkaHealth() {
        return kafkaHealthIndicator.health().getStatus() == Status.UP;
    }

    private boolean checkCockroachDBHealth() {
        return cockroachDBHealthIndicator.health().getStatus() == Status.UP;
    }

    private boolean checkServiceManagerHealth() {
        return AAAHealthIndicator.health().getStatus() == Status.UP;
    }
}*/