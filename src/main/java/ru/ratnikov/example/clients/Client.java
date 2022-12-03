package ru.ratnikov.example.clients;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.apache.hc.client5.http.fluent.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class Client {

    public static void main(String[] args) {

        String host = "http://localhost:8080";
        int read = 0;
        int write = 0;
        List<Integer> ids = new ArrayList<>();

        try {
            host = args[0];
            read = Integer.parseInt(args[1]);
            write = Integer.parseInt(args[2]);
            String[] idsString = args[3].split(",");
            ids = Stream.of(idsString).map(Integer::parseInt).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("wrong args. expected: host_with_port readCount writeCount id1,id2,id3...");
            System.exit(1);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(read + write);

        for (int i = 0; i < read; i++) {
            int id = i < ids.size() ? i : i % ids.size();
            executorService.submit(new Reader(host, ids.get(id)));
        }

        for (int i = 0; i < write; i++) {
            int id = i < ids.size() ? i : i % ids.size();
            executorService.submit(new Writer(host, ids.get(id)));
        }
    }
}

@AllArgsConstructor
class Reader implements Runnable {
    private final String host;
    private final int id;

    @Override
    public void run() {
        while (true) {
            try {
                Request.Get(host + "/account/" + id).execute().returnContent();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}

@AllArgsConstructor
class Writer implements Runnable {
    private final String host;
    private final int id;

    @Override
    public void run() {
        while (true) {
            Random random = new Random();
            long amount = -30 + random.nextInt(100);
            try {
                Request.Put(host + "/account/" + id + "/" + amount).execute().returnContent();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}