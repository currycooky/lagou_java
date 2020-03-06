package com.curry;

import com.curry.annotation.Autowired;
import com.curry.annotation.Repository;
import com.curry.annotation.Service;
import com.curry.dao.impl.AccountDaoImpl;
import com.curry.entity.Account;
import com.curry.pkgscanner.PkgScanner;
import com.curry.proxy.ProxyFactory;
import com.curry.service.AccountService;
import com.curry.utils.ScanUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.channels.ClosedSelectorException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 容器启动类，扫描加载所有的bean
 *
 * @author curry
 */
public class ApplicationContext {
    /**
     * 存储全部的bean
     */
    private static final Map<String, Object> beanMap;

    static {
        beanMap = new HashMap<>();
        // 获取到当前项目的根路径
        String packageFullName = ApplicationContext.class.getPackage().getName();
        String packageRootName = packageFullName.substring(0, packageFullName.indexOf("."));
        List<Class<?>> classsFromPackage = ScanUtils.getClasssFromPackage(packageRootName);
        // 遍历扫描出来的类
        for (Class<?> aClass : classsFromPackage) {
            // 获取类上的注解
            Service service = aClass.getAnnotation(Service.class);
            Repository repository = aClass.getAnnotation(Repository.class);
            if (service != null && repository != null) {
                throw new RuntimeException("配置了重复的bean");
            }
            if (service != null) {
                saveBean(service.value(), aClass);
            }
            if (repository != null) {
                saveBean(repository.value(), aClass);
            }
        }
    }

    /**
     * 给指定的类型实例出一个对象
     *
     * @param clazz
     * @return
     */
    private static Object newInstatnce(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("实例对象出错");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("实例对象出错");
        }
    }

    /**
     * 将bean保存到map集合中
     *
     * @param value
     * @param clazz
     */
    private static void saveBean(String value, Class<?> clazz) {
        String beanName = value;
        if (beanName == null || "".equals(beanName)) {
            Class<?>[] interfaces = clazz.getInterfaces();
            // 如果没有继承，以当前类的类名作为bean的名称
            if (interfaces == null || interfaces.length < 1 || interfaces.length > 1) {
                beanName = clazz.getSimpleName();
            } else {
                beanName = interfaces[0].getSimpleName();
            }
        }
        Object instance = newInstatnce(clazz);
        // 给实例出来的对象中，带有@Auto注解的属性赋值
        setObj(instance);
        beanMap.put(beanName, ProxyFactory.getJdkProxy(instance));
    }

    /**
     * 给类中的带有@Autowired的属性注入值
     *
     * @param obj
     */
    private static void setObj(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (autowired == null) {
                continue;
            }
            String beanName = autowired.value();
            if (beanName == null || "".equals(beanName)) {
                // 如果没有设置指定的bean，则以当前属性的类型作为名称
                beanName = field.getType().getSimpleName();
            }
            // 暴力访问
            field.setAccessible(true);
            try {
                field.set(obj, beanMap.get(beanName));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, Object> getBeans() {
        return beanMap;
    }

    public <T> T getBean(String beanName, Class<T> clazz) {
        return (T) beanMap.get(beanName);
    }

    public static void main(String[] args) throws SQLException {
        ApplicationContext applicationContext = new ApplicationContext();
        AccountService accountService = applicationContext.getBean("AccountService", AccountService.class);
        // 收款账号
        Account to = new Account();
        to.setCardNo("1");
        // 扣款账号
        Account from = new Account();
        from.setCardNo("2");
        from.setBalance(100D);
        accountService.transfer(to, from);

        System.out.println("转账成功...");
    }
}
