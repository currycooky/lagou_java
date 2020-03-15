package com.lagou.test;

import com.lagou.dao.IUserDao;
import com.lagou.io.Resources;
import com.lagou.pojo.User;
import com.lagou.sqlSession.SqlSession;
import com.lagou.sqlSession.SqlSessionFactory;
import com.lagou.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 自定义持久层框架测试类
 *
 * @author curry
 */
public class IPersistenceTest {
    private SqlSession sqlSession;

    private IUserDao userDao;

    @Before
    public void openSession() throws PropertyVetoException, DocumentException {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        sqlSession = sqlSessionFactory.openSession();
        userDao = sqlSession.getMapper(IUserDao.class);
    }

    @Test
    public void findAll() throws Exception {
        List<User> all = userDao.findAll();
        all.forEach(System.out::println);
    }

    @Test
    public void insertTest() {
        User user = new User();
        user.setUsername(getNowDateTime());
        userDao.insertUser(user);
    }

    @Test
    public void updateTest() {
        User user = new User();
        user.setId(1);
        user.setUsername("update " + getNowDateTime());
        userDao.updateUsernameById(user);
    }

    @Test
    public void deleteTest() {
        User user = new User();
        user.setId(3);
        userDao.deleteById(user);
    }

    private String getNowDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
