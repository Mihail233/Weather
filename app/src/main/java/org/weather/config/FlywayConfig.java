package org.weather.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    //    Ключевые параметры
    //   .baselineVersion("0")     // Начальная версия миграций — автоматически создаёт базовую версию, если БД новая.
    //    .validateOnMigrate(false)  — если false, Flyway пропустит проверку целостности миграций (полезно при разработке).
    //    .outOfOrder(true)  — разрешает применять миграции не в порядке версий.
    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)  // Источник данных (БД)
                .baselineOnMigrate(true)  // Создаёт baseline при первом запуске
                .locations("classpath:db")
                .load();
    }
}
