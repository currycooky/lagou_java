package com.lagou.sqlSession;

/**
 * 数据库连接工厂
 *
 * @author curry
 */
public interface SqlSessionFactory {
    /**
     * 开启数据库连接
     *
     * @return
     */
    SqlSession openSession();
}
