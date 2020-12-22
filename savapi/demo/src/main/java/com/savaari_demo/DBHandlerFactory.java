package com.savaari_demo;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class DBHandlerFactory {

    private static DBHandlerFactory instance = null;
    private DBHandler dbHandler = null;

    private DBHandlerFactory() {

    }

    public static DBHandlerFactory getInstance() {
        if (instance == null) {
            instance = new DBHandlerFactory();
        }
        return instance;
    }

    public DBHandler getDBHandler() {

        Properties prop = null;
        String propFileName = "config.properties";
        InputStream inputStream = null;

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

            //TODO: figure out how to instantiate singleton & should OracleDBHandler be a singleton?
            return (DBHandler) Class.forName(dbHandlerClassName).newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
