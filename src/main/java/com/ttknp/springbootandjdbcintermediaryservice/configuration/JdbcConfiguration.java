package com.ttknp.springbootandjdbcintermediaryservice.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;
import java.util.Objects;


// **** Change to config at runtime on CMD
// @Configuration // Config Datasource by Java
// @PropertySource(value="classpath:info/db.properties", ignoreResourceNotFound=true)
public class JdbcConfiguration {

    private static final Logger log = LoggerFactory.getLogger(JdbcConfiguration.class);
    private final Environment env;

    // @Autowired
    public JdbcConfiguration(Environment env) {
        this.env = env;
    }

    // @Bean
    public DataSource dataSource() {
        var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("jdbc.driver")));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.username"));
        dataSource.setPassword(env.getProperty("jdbc.password"));
        return dataSource;
    }

    // @Bean
    public JdbcTemplate jdbcTemplate() {
        var template = new JdbcTemplate();
        template.setDataSource(dataSource());
        return template;
    }

    // @Bean // for using with BeanPropertySqlParameterSource
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        var template = new NamedParameterJdbcTemplate(dataSource());
        return template;
    }
}
