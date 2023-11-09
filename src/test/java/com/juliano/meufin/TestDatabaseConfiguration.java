package com.juliano.meufin;


import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@org.springframework.boot.test.context.TestConfiguration
public class TestDatabaseConfiguration {
    @Bean
    @Primary
    public DataSource dataSource() {

        return DataSourceBuilder
                .create()
                .url("jdbc:mysql://localhost/api_test")
                .username("root")
                .password("root123")
                .driverClassName("com.mysql.cj.jdbc.Driver")

                .build();
    }
}
