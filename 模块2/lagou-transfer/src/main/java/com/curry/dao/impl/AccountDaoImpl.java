package com.curry.dao.impl;

import com.curry.annotation.Repository;
import com.curry.dao.AccountDao;
import com.curry.entity.Account;
import com.curry.factory.ConnectionFactory;
import com.curry.utils.ConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 账户持久层具体实现
 *
 * @author curry
 */
@Repository
public class AccountDaoImpl implements AccountDao {
    private ConnectionFactory connectionFactory = new ConnectionFactory();

    @Override
    public void substract(String cardNo, Double money) throws SQLException {
        update(cardNo, money, "-");
    }

    @Override
    public void add(String cardNo, Double money) throws SQLException {
        update(cardNo, money, "+");
    }

    /**
     * 执行更新操作
     *
     * @param cardNo
     * @param money
     * @param type
     * @throws SQLException
     */
    private void update(String cardNo, Double money, String type) throws SQLException {
        Connection connection = ConnectionUtils.getInstance();
        String sql = "update account set balance = (balance " + type + " ?) where card_no = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDouble(1, money);
        preparedStatement.setString(2, cardNo);
        preparedStatement.execute();
        preparedStatement.close();
    }

    @Override
    public Account query(String cardNo) throws SQLException {
        Connection connection = ConnectionUtils.getInstance();
        String sql = "select * from account where card_no = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, cardNo);
        ResultSet resultSet = preparedStatement.executeQuery();
        Account account = new Account();
        while (resultSet.next()) {
            account.setName(resultSet.getString("name"));
            account.setBalance(resultSet.getDouble("balance"));
            account.setCardNo(resultSet.getString("card_no"));
        }
        preparedStatement.close();
        return account;
    }
}
