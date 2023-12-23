package ir.bigz.springboot.springmuldatasource.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.init.DataSourceScriptDatabaseInitializer;
import org.springframework.boot.sql.init.DatabaseInitializationMode;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.List;

@Configuration(proxyBeanMethods = false)
public class DataSourceConfiguration {

    //Blog Config -------------------------------

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.blog")
    public DataSourceProperties blogDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public HikariDataSource blogDataSource(DataSourceProperties blogDataSourceProperties) {
        return blogDataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public DataSourceScriptDatabaseInitializer blogDataSourceScriptDatabaseInitializer(@Qualifier("blogDataSource") DataSource blogDataSource) {
        var settings = new DatabaseInitializationSettings();
        settings.setSchemaLocations(List.of("classpath:blog-schema.sql"));
        settings.setMode(DatabaseInitializationMode.EMBEDDED);
        return new DataSourceScriptDatabaseInitializer(blogDataSource, settings);
    }

    //Subscribers Config -------------------------------

    @Bean
    @ConfigurationProperties("app.datasource.subscribers")
    public DataSourceProperties subscriberDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource subscriberDataSource(@Qualifier("subscriberDataSourceProperties") DataSourceProperties subscriberDataSourceProperties) {
        return DataSourceBuilder
                .create()
                .url(subscriberDataSourceProperties.getUrl())
                .username(subscriberDataSourceProperties.getUsername())
                .password(subscriberDataSourceProperties.getPassword())
                .build();
    }

    @Bean
    public DataSourceScriptDatabaseInitializer subscriberDataSourceScriptDatabaseInitializer(@Qualifier("subscriberDataSource") DataSource subscriberDataSource) {
        var settings = new DatabaseInitializationSettings();
        settings.setSchemaLocations(List.of("classpath:subscriber-schema.sql"));
        settings.setMode(DatabaseInitializationMode.EMBEDDED);
        return new DataSourceScriptDatabaseInitializer(subscriberDataSource, settings);
    }
}
