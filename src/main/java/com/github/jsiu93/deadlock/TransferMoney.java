package com.github.jsiu93.deadlock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransferMoney implements  Runnable{

    int flag = 1;

    static Account a = new Account(500);
    static Account b = new Account(500);
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        TransferMoney r1 = new TransferMoney();
        TransferMoney r2 = new TransferMoney();
        r1.flag = 1;
        r2.flag = 0;

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.info("a的余额:{}", a.balance);
        log.info("b的余额:{}", b.balance);

    }

    @Override
    public void run() {
        if (flag == 1) {
            transfer(a, b, 200);

        }
        if (flag == 0) {
            transfer(b, a, 200);
        }

    }

    public static void transfer(Account from, Account to, int amount) {
        class Helper{
            public void transfer() {
                if (from.balance - amount < 0) {
                    log.info("余额不足，转账失败");
                }
                from.balance -= amount;
                to.balance = to.balance + amount;
                log.info("转账成功, {}元", amount);
            }
        }
        int fromHash = System.identityHashCode(from);
        int toHash = System.identityHashCode(to);
        if (fromHash < toHash) {
            synchronized (from) {
                synchronized (to) {
                    new Helper().transfer();
                }
            }
        } else if (fromHash > toHash) {
            synchronized (to) {
                synchronized (from) {
                    new Helper().transfer();
                }
            }
        } else {
            synchronized (lock) {
                synchronized (to) {
                    synchronized (from) {
                        new Helper().transfer();
                    }
                }
            }
        }

    }

    static class Account {
        int balance;

        public Account(int balance) {
            this.balance = balance;
        }
    }
}
