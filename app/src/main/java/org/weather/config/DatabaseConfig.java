package org.weather.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.hibernate.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:/application.properties")
public class DatabaseConfig {

    @Value("${db.driver}")
    private String databaseDriver;

    @Value("${db.url}")
    private String databaseUrl;

    @Value("${db.username}")
    private String databaseUsername;

    @Value("${db.password}")
    private String databasePassword;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hibernateHbm2DdlAuto;

    @Value("${hibernate.format_sql}")
    private String hibernateFormatSql;

    @Value("${hibernate.default_schema}")
    private String hibernateSchema;

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSourceClassName(databaseDriver);
        hikariConfig.setJdbcUrl(databaseUrl);
        hikariConfig.setUsername(databaseUsername);
        hikariConfig.setPassword(databasePassword);
        hikariConfig.setPoolName("myHicariCp");

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    @DependsOn("flyway")
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(dataSource);
        localSessionFactoryBean.setPackagesToScan("org/weather/entity");
        localSessionFactoryBean.setHibernateProperties(getHibernateProperties());
        return localSessionFactoryBean;
    }


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

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", hibernateDialect);
        properties.setProperty("hibernate.show_sql", hibernateShowSql);
        properties.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2DdlAuto);
        properties.setProperty("hibernate.format_sql", hibernateFormatSql);
        properties.setProperty("hibernate.default_schema", hibernateSchema);
        return properties;
    }
}