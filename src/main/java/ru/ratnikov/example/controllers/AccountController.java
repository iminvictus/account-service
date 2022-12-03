package ru.ratnikov.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.ratnikov.example.services.AccountService;
import ru.ratnikov.example.util.AccountStatistic;

@RestController
public class AccountController {
    private final AccountService service;

    private final AccountStatistic statistic;

    @Autowired
    public AccountController(AccountService service, AccountStatistic statistic) {
        this.service = service;
        this.statistic = statistic;
    }

    @GetMapping("/account/{id}")
    public Long getAmount(@PathVariable Integer id) {
        return service.getAmount(id);
    }

    @PutMapping("/account/{id}/{amount}")
    public void addAmount(@PathVariable Integer id, @PathVariable Long amount) {
        service.addAmount(id, amount);
    }

    @GetMapping("/get-statistic")
    public String getStatistic() {
        return "read:" + statistic.getCurrentRead() + " write:" + statistic.getCurrentWrite() + "<br/>"
                + "readTotal:" + statistic.getReadTotal() + " writeTotal:" + statistic.getWriteTotal();
    }

    @GetMapping("/reset-statistic")
    public void resetStatistic() {
        statistic.reset();
    }
}
