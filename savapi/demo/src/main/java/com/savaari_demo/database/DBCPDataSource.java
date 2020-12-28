package com.savaari_demo.database;

import com.mysql.cj.jdbc.Driver;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBCPDataSource {
    
    private static BasicDataSource dataSource = new BasicDataSource();
    
    static {
        dataSource.setUrl("jdbc:mysql://"
                + "localhost/ride_hailing" + "?user=" + "root"
                + "&password=" + "thisIsOurSDAProjectPassword" +
                "&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Karachi");
        dataSource.setUsername("root");
        dataSource.setPassword("thisIsOurSDAProjectPassword");
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        try {
            dataSource.setDriver((Driver) Class.forName("com.mysql.cj.jdbc.Driver").newInstance());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        dataSource.setMaxOpenPreparedStatements(100);
    }
    
    public synchronized static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    private DBCPDataSource(){ }
}