package com.curry.proxy;

import com.curry.annotation.CurryTransactional;
import com.curry.factory.ConnectionFactory;
import com.curry.utils.ConnectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * 获取代理类工厂
 *
 * @author curry
 */
public class ProxyFactory {
    private ProxyFactory() {
    }

    /**
     * 获取指定对象的代理类
     *
     * @param obj
     * @return
     */
    public static Object getJdkProxy(Object obj) {
        // 获取代理对象
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), (proxy, method, args) -> {
            // 如果没有配置事务回滚，则直接提交
            if (method.getAnnotation(CurryTransactional.class) == null) {
                // 查看当前子类实现的方法是否加了注解
                Method[] declaredMethods = obj.getClass().getDeclaredMethods();
                Method implMethod = null;
                for (Method declaredMethod : declaredMethods) {
                    if (declaredMethod.getName().equals(method.getName())) {
                        implMethod = declaredMethod;
                        break;
                    }
                }
                if (implMethod == null || implMethod.getAnnotation(CurryTransactional.class) == null) {
                    return method.invoke(obj, args);
                }
            }
            Connection connection = ConnectionUtils.getInstance();
            connection.setAutoCommit(false);
            try {
                Object result = method.invoke(obj, args);
                // 提交事务
                connection.commit();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                // 回滚事务
                connection.rollback();
                // 抛出异常便于上层servlet捕获
                throw e;
            }
        });
    }

}
