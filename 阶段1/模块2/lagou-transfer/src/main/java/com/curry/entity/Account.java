package com.curry.entity;

/**
 * 用户账户余额
 *
 * @author curry
 */
public class Account {
    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户卡号
     */
    private String cardNo;

    /**
     * 用户余额
     */
    private Double balance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", balance=" + balance +
                '}';
    }
}
