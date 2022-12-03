package ru.ratnikov.example.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ratnikov.example.models.Account;
import ru.ratnikov.example.repositories.AccountRepository;

import java.util.Optional;

@Service
@Slf4j
public class AccountRepositoryService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountRepositoryService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void createNew(Integer id, Long addAmount) {
        Account account = new Account(id, addAmount);
        accountRepository.save(account);
        log.debug("Created account:{}", account);
    }


    @Transactional
    public void addToExist(Integer id, Long addAmount, Long toCompare) {
        log.debug("addToExist invoked id:{} addAmount:{} toCompare{}", id, addAmount, toCompare);

        Optional<Account> accountOpt = accountRepository.findById(id);
        if (accountOpt.isEmpty()) {
            log.error("Account with id {} not found", id);
            System.exit(1);
        }

        Account account = accountOpt.get();
        Long dbAmount = account.getAmount();

        if (toCompare != dbAmount.longValue()) {
            log.error("Database and cached account amount differ, id:{} cached:{} db:{}", id, toCompare, dbAmount);
            System.exit(1);
        }

        account.addAmount(addAmount);
        accountRepository.save(account);
        log.debug("Updated account: {}", account);
    }
}
