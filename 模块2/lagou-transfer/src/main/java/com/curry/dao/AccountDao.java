package com.curry.dao;

import com.curry.entity.Account;

import java.sql.SQLException;

/**
 * 账户持久层接口
 *
 * @author curry
 */
public interface AccountDao {
    /**
     * 账户余额减少
     *
     * @param cardNo 卡号
     * @param money 金额
     */
    void substract(String cardNo, Double money) throws SQLException;

    /**
     * 账户余额增加
     *
     * @param cardNo 卡号
     * @param money 金额
     * @throws SQLException
     */
    void add(String cardNo, Double money) throws SQLException;

    /**
     * 查询账户信息
     *
     * @param cardNo 卡号
     * @return
     */
    Account query(String cardNo) throws SQLException;
}
