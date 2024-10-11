package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.UsernameBalanceDto;

public interface AccountDao {
    UsernameBalanceDto checkBalance(String username);

    void updateBalance(String username, double transferAmount);

    //create();
}
