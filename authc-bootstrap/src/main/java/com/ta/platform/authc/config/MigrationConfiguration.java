package com.ta.platform.authc.config;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Flyway.class)
@Slf4j
public class MigrationConfiguration {

    @Bean
    public FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
//        return new FlywayMigrationInitializer(flyway, new FlywayMigrationStrategy() {
//            @Override
//            public void migrate(Flyway flyway) {
//                try {
//                    flyway.migrate();
//                } catch (FlywayException e) {
//                    log.error("Flyway migration failed, doing a repair and retrying ...");
//                    flyway.repair();
//                    flyway.migrate();
//                }
//            }
//        });
        return new FlywayMigrationInitializer(flyway, (flyway1) -> {
            try {
                flyway.migrate();
            } catch (FlywayException e) {
                log.error("Flyway migration failed, doing a repair and retrying ...");
                flyway.repair();
                flyway.migrate();
            }
        });
    }
}