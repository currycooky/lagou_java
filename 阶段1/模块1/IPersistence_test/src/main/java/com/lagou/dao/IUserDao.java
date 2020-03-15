package com.lagou.dao;

import com.lagou.pojo.User;

import java.util.List;

/**
 * 用户持久层接口
 *
 * @author curry
 */
public interface IUserDao {
    /**
     * 查询所有用户
     *
     * @return
     * @throws Exception
     */
    List<User> findAll() throws Exception;

    /**
     * 根据条件进行用户查询
     *
     * @param user
     * @return
     * @throws Exception
     */
    User findByCondition(User user) throws Exception;

    /**
     * 插入一个新的用户
     *
     * @param user
     */
    void insertUser(User user);

    /**
     * 根据用户id修改用户的名字
     *
     * @param user
     */
    void updateUsernameById(User user);

    /**
     * 根据用户id删除用户信息
     *
     * @param user
     */
    void deleteById(User user);
}
