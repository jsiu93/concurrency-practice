package com.github.jsiu93.deadlock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeadLock implements Runnable {

    static Object o1 = new Object();
    static Object o2 = new Object();

     int flag = 1;

    public static void main(String[] args) {
        DeadLock r1 = new DeadLock();
        DeadLock r2 = new DeadLock();
        r1.flag = 1;
        r2.flag = 0;
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();

    }


    @Override
    public void run() {
        if (flag == 1) {
            synchronized (o1) {
                log.info("线程1获取到o1锁");
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o2) {
                    log.info("线程1获取到o2锁");
                }
            }
        }
        if (flag == 0) {
            synchronized (o2) {
                log.info("线程2获取到o2锁");
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o1) {
                    log.info("线程2获取到o1锁");
                }
            }
        }

    }
}
