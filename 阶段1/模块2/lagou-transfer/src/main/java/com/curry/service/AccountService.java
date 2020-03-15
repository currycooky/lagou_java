package com.curry.service;

import com.curry.entity.Account;

import java.sql.SQLException;

/**
 * 业务逻辑层接口
 *
 * @author curry
 */
public interface AccountService {
    /**
     * 转账操作
     *
     * @param to 收款方信息
     * @param from 付款方信息
     */
    void transfer(Account to, Account from) throws SQLException;
}
