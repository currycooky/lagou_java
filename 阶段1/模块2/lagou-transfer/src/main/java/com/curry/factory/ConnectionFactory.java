package com.curry.factory;

import com.curry.utils.DruidUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author curry
 */
public class ConnectionFactory {
    /**
     * 当前线程存储的连接池对象
     */
    private ThreadLocal<Connection> druidDataSourceThreadLocal = new ThreadLocal<>();

    public Connection getCurrentThreadConn() throws SQLException {
        Connection connection = druidDataSourceThreadLocal.get();
        if (connection == null) {
            // 从连接池拿连接并绑定到线程
            connection = DruidUtils.getInstance().getConnection();
            // 绑定到当前线程
            druidDataSourceThreadLocal.set(connection);
        }
        return connection;
    }
}
