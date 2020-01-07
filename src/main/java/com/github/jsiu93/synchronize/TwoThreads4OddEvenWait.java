package com.github.jsiu93.synchronize;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Xiao Zhuhua
 * @date 2020/1/7 10:00 AM
 * @since 1.0.0
 */
@Slf4j
public class TwoThreads4OddEvenWait implements Runnable {

    private final Object lock = new Object();
    private int count;

    @Override
    public void run() {
        while (count <= 100) {
            synchronized (lock) {
                log.info("{}", count++);
                lock.notify();
                if (count <= 100) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public static void main(String[] args) {

        TwoThreads4OddEvenWait target = new TwoThreads4OddEvenWait();
        Thread t1 = new Thread(target);
        t1.start();
        Thread t2 = new Thread(target);
        t2.start();
    }
}
