package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * 执行器接口
 *
 * @author curry
 */
public interface Executor {
    /**
     * 查询执行方法
     *
     * @param configuration
     * @param mappedStatement
     * @param params
     * @param <E>
     * @return
     * @throws Exception
     */
    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    /**
     * 插入/修改/删除数据
     *
     * @param configuration
     * @param mappedStatement
     * @param obj
     * @throws SQLException
     */
    void execute(Configuration configuration, MappedStatement mappedStatement, Object obj) throws Exception;
}
