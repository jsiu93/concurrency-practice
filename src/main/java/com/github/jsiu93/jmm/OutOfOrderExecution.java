package com.github.jsiu93.jmm;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class OutOfOrderExecution {

    private static int x, y = 0;
    private static int a, b = 0;


    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        for (; ; ) {
            i++;
            x = 0;
            y = 0;
            a = 0;
            b = 0;

            CountDownLatch latch = new CountDownLatch(3);

            Thread t1 = new Thread(() -> {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                a = 1;
                x = b;
            });

            Thread t2 = new Thread(() -> {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                b = 1;
                y = a;
            });

            t1.start();
            t2.start();
            latch.countDown();
            t1.join();
            t2.join();

            if (x == 1 && y == 1) {
                log.info("第{}次, x={}, y={}", i, x, y);
                break;
            } else {
                log.info("第{}次, x={}, y={}", i, x, y);
            }
        }

    }


}
