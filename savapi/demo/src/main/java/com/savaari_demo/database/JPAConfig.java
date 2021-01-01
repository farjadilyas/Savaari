package com.savaari_demo.database;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JPAConfig {

    @Bean(name = "mySqlDataSource")
    public static DataSource getDataSource()
    {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://"
                + "localhost/ride_hailing" + "?user=" + "root"
                + "&password=" + "thisIsOurSDAProjectPassword" +
                "&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Karachi");
        return dataSourceBuilder.build();
    }
}