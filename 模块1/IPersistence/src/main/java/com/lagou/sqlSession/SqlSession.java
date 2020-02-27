package com.lagou.sqlSession;

import java.util.List;

/**
 * 数据的基础操作方法
 *
 * @author curry
 */
public interface SqlSession {

    /**
     * 查询所有
     *
     * @param statementId
     * @param params
     * @return
     * @throws Exception
     */
    <E> List<E> selectList(String statementId, Object... params) throws Exception;

    /**
     * 根据条件查询单个
     *
     * @param statementId
     * @param params
     * @return
     * @throws Exception
     */
    <T> T selectOne(String statementId, Object... params) throws Exception;

    /**
     * 插入/修改/删除数据
     *
     * @param obj
     */
    void execute(String statementId, Object obj) throws Exception;

    /**
     * 为Dao接口生成代理实现类
     *
     * @param mapperClass
     * @param <T>
     * @return
     */
    <T> T getMapper(Class<T> mapperClass);
}
