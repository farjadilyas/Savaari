package com.savaari_demo;

public class DBHandlerFactory {

    public static DBHandler getDBHandler(String handlerType) {
        if (handlerType.equals("Oracle")) {
            return new OracleDBHandler();
        }
        else {
            return null;
        }
    }
}
