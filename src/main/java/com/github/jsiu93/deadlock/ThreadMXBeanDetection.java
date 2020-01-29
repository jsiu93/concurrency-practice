package com.github.jsiu93.deadlock;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadMXBeanDetection implements Runnable {
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

        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
        if (deadlockedThreads != null && deadlockedThreads.length > 0) {
            for (int i = 0; i < deadlockedThreads.length; i++) {
                ThreadInfo threadInfo = threadMXBean.getThreadInfo(deadlockedThreads[i]);
                log.info("出现死锁, {}", threadInfo.getThreadName());
            }
        }

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
