package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccounts();

    Account findById(Long id);

    void save(Account account);

    boolean existsByNumber(String accountNumber);

    Account findByNumber(String number);

}
