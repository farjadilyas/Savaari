package com.savaari_demo.database;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class DBHandlerFactory {

    private static DBHandlerFactory instance = null;

    private DBHandlerFactory() {

    }

    public synchronized static DBHandlerFactory getInstance() {
        if (instance == null) {
            instance = new DBHandlerFactory();
        }
        return instance;
    }

    public DBHandler createDBHandler() {

        Properties prop;
        String propFileName = "config.properties";
        InputStream inputStream;

        try {
            prop = new Properties();
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // Get property value
            String dbHandlerClassName = prop.getProperty("dbHandler");

            return (DBHandler) Class.forName(dbHandlerClassName).getDeclaredConstructor().newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
