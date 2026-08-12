package org.weather.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.hibernate.HibernateTransactionManager;
import org.springframework.orm.jpa.hibernate.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:/application.properties")
public class DatabaseConfig {

    private static final String HIBERNATE_DIALECT_KEY = "hibernate.dialect";
    private static final String HIBERNATE_HBM_2_DDL_AUTO_KEY = "hibernate.hbm2ddl.auto";
    private static final String HIBERNATE_SHOW_SQL_KEY = "hibernate.show_sql";
    private static final String HIBERNATE_FORMAT_SQL_KEY = "hibernate.format_sql";
    private static final String HIBERNATE_HIGHLIGHT_SQL_KEY = "hibernate.highlight_sql";
    private static final String HIBERNATE_DEFAULT_SCHEMA_KEY = "hibernate.default_schema";


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

    @Value("${hibernate.highlight_sql}")
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
    @DependsOn("flyway")
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(dataSource);
        localSessionFactoryBean.setPackagesToScan("org.weather.entity");
        localSessionFactoryBean.setHibernateProperties(hibernateProperties());
        return localSessionFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
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