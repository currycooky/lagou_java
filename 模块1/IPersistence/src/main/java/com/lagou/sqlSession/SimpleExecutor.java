package com.lagou.sqlSession;


import com.lagou.config.BoundSql;
import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import com.lagou.utils.GenericTokenParser;
import com.lagou.utils.ParameterMapping;
import com.lagou.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单的默认执行器接口的实现
 *
 * @author curry
 */
public class SimpleExecutor implements Executor {

    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        // 1. 注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();

        // 2. 获取sql语句 : select * from user where id = #{id} and username = #{username}
        //转换sql语句： select * from user where id = ? and username = ? ，转换的过程中，还需要对#{}里面的值进行解析存储
        BoundSql boundSql = getBoundSql(mappedStatement);

        // 3.获取预处理对象：preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        // 4. 设置参数
        // 获取到了参数的全路径
        String parameterType = mappedStatement.getParameterType();
        Class<?> parameterTypeClass = getClassType(parameterType);

        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        setObj(parameterMappingList, parameterTypeClass, params == null ? null : params[0], preparedStatement);

        // 5. 执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);

        ArrayList<Object> objects = new ArrayList<>();

        // 6. 封装返回结果集
        while (resultSet.next()) {
            Object o = resultTypeClass.newInstance();
            //元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {

                // 字段名
                String columnName = metaData.getColumnName(i);
                // 字段的值
                Object value = resultSet.getObject(columnName);

                //使用反射或者内省，根据数据库表和实体的对应关系，完成封装
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, value);
            }
            objects.add(o);
        }
        return (List<E>) objects;
    }

    @Override
    public void execute(Configuration configuration, MappedStatement mappedStatement, Object obj) throws Exception {
        Connection connection = getConnection(configuration);
        BoundSql boundSql = getBoundSql(mappedStatement);
        // 获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        Class<?> objClass = obj.getClass();
        setObj(parameterMappingList, objClass, obj, preparedStatement);
        preparedStatement.execute();
        preparedStatement.close();
    }

    private void setObj(List<ParameterMapping> parameterMappingList, Class<?> parameterTypeClass, Object obj, PreparedStatement preparedStatement) throws IllegalAccessException, SQLException, NoSuchFieldException {
        if (parameterMappingList == null || parameterMappingList.size() < 1) {
            return;
        }
        if (parameterTypeClass == null) {
            return;
        }
        if (obj == null) {
            return;
        }
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            // 反射
            Field declaredField = parameterTypeClass.getDeclaredField(content);
            //暴力访问
            declaredField.setAccessible(true);
            Object o = declaredField.get(obj);

            preparedStatement.setObject(i + 1, o);
        }

    }
    /**
     * 获取数据库连接
     *
     * @param configuration
     * @return
     * @throws SQLException
     */
    private Connection getConnection(Configuration configuration) throws SQLException {
        return configuration.getDataSource().getConnection();
    }

    /**
     * 获取参数的类型
     *
     * @param parameterType
     * @return
     * @throws ClassNotFoundException
     */
    private Class<?> getClassType(String parameterType) throws ClassNotFoundException {
        if (parameterType != null) {
            Class<?> aClass = Class.forName(parameterType);
            return aClass;
        }
        return null;
    }


    /**
     * 完成对#{}的解析工作：1.将#{}使用？进行代替，2.解析出#{}里面的值进行存储
     *
     * @param mappedStatement
     * @return
     */
    private BoundSql getBoundSql(MappedStatement mappedStatement) {
        String sql = mappedStatement.getSql();
        // 标记处理类：配置标记解析器来完成对占位符的解析处理工作
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        // 解析出来的sql
        String parseSql = genericTokenParser.parse(sql);
        // #{}里面解析出来的参数名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        BoundSql boundSql = new BoundSql(parseSql, parameterMappings);
        return boundSql;
    }


}
