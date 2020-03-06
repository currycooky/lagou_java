package com.curry.service.impl;

import com.curry.annotation.Autowired;
import com.curry.annotation.Service;
import com.curry.annotation.CurryTransactional;
import com.curry.dao.AccountDao;
import com.curry.entity.Account;
import com.curry.service.AccountService;

import java.sql.SQLException;

/**
 * 业务逻辑层具体实现类
 *
 * @author curry
 */
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    @Override
    @CurryTransactional
    public void transfer(Account to, Account from) throws SQLException {
        // 具体转账的金额
        Double money = from.getBalance();
        // 给收款方增加金额
        accountDao.add(to.getCardNo(), money);
        // TODO 手动抛出一个异常
        if (true) {
            throw new RuntimeException("测试数据回滚");
        }
        // 给付款方减少金额
        accountDao.substract(from.getCardNo(), money);
    }
}
