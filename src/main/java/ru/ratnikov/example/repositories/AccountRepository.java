package ru.ratnikov.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ratnikov.example.models.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

}
