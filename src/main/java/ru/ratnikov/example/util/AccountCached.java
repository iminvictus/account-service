package ru.ratnikov.example.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountCached {
    private int id;
    private long value;
    private boolean exists;

    public void addAmount(Long amount) {
        this.value += amount;
    }
}
