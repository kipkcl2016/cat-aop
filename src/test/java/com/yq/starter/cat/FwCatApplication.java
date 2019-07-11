package com.yq.starter.cat;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yq
 * @date 2019-01-24
 */
@SpringBootApplication
@RestController
public class FwCatApplication implements CommandLineRunner {
    public static void main(String[] args) {
        System.out.println("444444444");
        SpringApplication.run(FwCatApplication.class, args);
//        Transaction t = Cat.newTransaction("URI", "pageName");
//        t.complete();

        System.out.println("5555555555");
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("FwCatApplication.run");
    }
}
