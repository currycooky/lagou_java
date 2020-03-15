package com.curry.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.jdbc.Driver;

/**
 * 获取数据库连接池对象
 *
 * @author curry
 */
public class DruidUtils {
    private DruidUtils() {}

    /**
     * 阿里数据库连接池
     */
    private static DruidDataSource druidDataSource;

    /**
     * 数据库驱动
     */
    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

    /**
     * 数据库连接
     */
    private static final String URL = "jdbc:mysql://localhost:3306/zdy_mybatis?characterEncoding=utf8";

    /**
     * 数据库账号
     */
    private static final String USERNAME = "root";

    /**
     * 数据库密码
     */
    private static final String PASSWORD = "root";

    /**
     * 获取数据库连接池
     *
     * @return
     */
    public static final DruidDataSource getInstance() {
        if (druidDataSource == null) {
            druidDataSource = new DruidDataSource();
            druidDataSource.setDriverClassName(DRIVER_CLASS_NAME);
            druidDataSource.setUrl(URL);
            druidDataSource.setUsername(USERNAME);
            druidDataSource.setPassword(PASSWORD);
        }
        return druidDataSource;
    }
}
