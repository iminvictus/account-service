package ru.ratnikov.example.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AccountStatistic {
    private AtomicInteger totalRead = new AtomicInteger();
    private AtomicInteger totalWrite = new AtomicInteger();

    private int prevRead;
    private int prevWrite;

    private int currentRead;
    private int currentWrite;

    public void addWrite() {
        totalWrite.incrementAndGet();
    }

    public void addRead() {
        totalRead.incrementAndGet();
    }

    public int getWriteTotal() {
        return totalWrite.get();
    }

    public int getReadTotal() {
        return totalRead.get();
    }

    public int getCurrentRead() {
        return currentRead;
    }

    public int getCurrentWrite() {
        return currentWrite;
    }

    public void reset() {
        totalRead.set(0);
        totalWrite.set(0);
        prevRead = 0;
        prevWrite = 0;
        currentRead = 0;
        currentWrite = 0;
    }

    @Scheduled(fixedDelay = 1000)
    private void calculateCurrent() {
        int totalRead = this.totalRead.get();
        int totalWrite = this.totalWrite.get();

        currentRead = totalRead - prevRead;
        currentWrite = totalWrite - prevWrite;

        prevRead = totalRead;
        prevWrite = totalWrite;
    }
}
