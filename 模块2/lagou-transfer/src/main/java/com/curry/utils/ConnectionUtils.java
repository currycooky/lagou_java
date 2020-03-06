package com.curry.utils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author curry
 */
public class ConnectionUtils {
    private ConnectionUtils() {}

    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    public static Connection getInstance() {
        Connection connection = connectionThreadLocal.get();
        if (connection == null) {
            try {
                connection = DruidUtils.getInstance().getConnection();
                connectionThreadLocal.set(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
