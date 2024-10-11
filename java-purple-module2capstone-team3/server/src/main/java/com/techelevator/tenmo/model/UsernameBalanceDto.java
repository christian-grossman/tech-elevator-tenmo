package com.techelevator.tenmo.model;

public class UsernameBalanceDto {

    public String username;
    public double balance;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "UsernameBalanceDto{" +
                "username='" + username + '\'' +
                ", balance=" + balance +
                '}';
    }
}
