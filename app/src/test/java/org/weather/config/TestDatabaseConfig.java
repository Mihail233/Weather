package org.weather.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:test.properties")
@ComponentScan("org.weather")
public class TestDatabaseConfig {

    private static final String HIBERNATE_DIALECT_KEY = "hibernate.dialect";
    private static final String HIBERNATE_HBM_2_DDL_AUTO_KEY = "hibernate.hbm2ddl.auto";
    private static final String HIBERNATE_SHOW_SQL_KEY = "hibernate.show_sql";
    private static final String HIBERNATE_FORMAT_SQL_KEY = "hibernate.format_sql";
    private static final String HIBERNATE_HIGHLIGHT_SQL_KEY = "hibernate.highlight_sql";
    private static final String HIBERNATE_DEFAULT_SCHEMA_KEY = "hibernate.default_schema";


    @Value("${test.db.driver}")
    private String databaseDriver;

    @Value("${test.db.url}")
    private String databaseUrl;

    @Value("${test.db.username}")
    private String databaseUsername;

    @Value("${test.db.password}")
    private String databasePassword;

    @Value("${test.hibernate.dialect}")
    private String hibernateDialect;

    @Value("${test.hibernate.show_sql}")
    private String hibernateShowSql;

    @Value("${test.hibernate.hbm2ddl.auto}")
    private String hibernateHbm2DdlAuto;

    @Value("${test.hibernate.format_sql}")
    private String hibernateFormatSql;

    @Value("${test.hibernate.default_schema}")
    private String hibernateSchema;

    @Value("${test.hibernate.highlight_sql}")
    private String hibernateHighlightSql;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(databaseDriver);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUsername);
        dataSource.setPassword(databasePassword);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan("org.weather.data.entity");

        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setJpaProperties(hibernateProperties());
        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty(HIBERNATE_DIALECT_KEY, hibernateDialect);
        properties.setProperty(HIBERNATE_SHOW_SQL_KEY, hibernateShowSql);
        properties.setProperty(HIBERNATE_HBM_2_DDL_AUTO_KEY, hibernateHbm2DdlAuto);
        properties.setProperty(HIBERNATE_FORMAT_SQL_KEY, hibernateFormatSql);
        properties.setProperty(HIBERNATE_DEFAULT_SCHEMA_KEY, hibernateSchema);
        properties.setProperty(HIBERNATE_HIGHLIGHT_SQL_KEY, hibernateHighlightSql);
        return properties;
    }
}
