package ru.ratnikov.example.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ratnikov.example.models.Account;
import ru.ratnikov.example.repositories.AccountRepository;
import ru.ratnikov.example.util.AccountCached;
import ru.ratnikov.example.util.AccountStatistic;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    private final AccountStatistic stat;

    private final AccountRepositoryService repositoryService;

    private final Map<Integer, AccountCached> cache = new ConcurrentHashMap<>();

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, AccountStatistic stat, AccountRepositoryService repositoryService) {
        this.accountRepository = accountRepository;
        this.stat = stat;
        this.repositoryService = repositoryService;
    }

    @PostConstruct
    private void init() {
        List<Account> accounts = accountRepository.findAll();
        accounts.forEach(a -> cache.put(a.getId(), new AccountCached(a.getId(), a.getAmount(), true)));
    }

    @Override
    public Long getAmount(Integer id) {
        stat.addRead();

        if (cache.containsKey(id)) {
            return cache.get(id).getValue();
        } else {
            return 0L;
        }
    }

    @Override
    public void addAmount(Integer id, Long addAmount) {
        stat.addWrite();

        log.debug("addAmount invoked for id:{} amount:{}", id, addAmount);

        cache.putIfAbsent(id, new AccountCached(id, 0, false));
        AccountCached holder = cache.get(id);

        synchronized (holder) {
            log.debug("addAmount synchronized by holder:{}", holder);

            if (holder.isExists()) {
                repositoryService.addToExist(id, addAmount, holder.getValue());
            } else {
                repositoryService.createNew(id, addAmount);
                holder.setExists(accountRepository.existsById(id));
            }

            holder.addAmount(addAmount);
            log.debug("addAmount synchronized by holder:{}", holder);
        }
    }
}
