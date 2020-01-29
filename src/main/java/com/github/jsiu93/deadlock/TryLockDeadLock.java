package com.github.jsiu93.deadlock;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class TryLockDeadLock implements Runnable {

    static Lock lock1 = new ReentrantLock();
    static Lock lock2 = new ReentrantLock();
    int flag = 1;

    public static void main(String[] args) {
        TryLockDeadLock r1 = new TryLockDeadLock();
        TryLockDeadLock r2 = new TryLockDeadLock();
        r1.flag = 1;
        r2.flag = 2;
        new Thread(r1).start();
        new Thread(r2).start();
    }



    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (flag == 1) {
                try {
                    if (lock1.tryLock(800, TimeUnit.MILLISECONDS)) {
                        TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
                        log.info("线程1获得lock1");
                        if (lock2.tryLock(800, TimeUnit.MILLISECONDS)) {
                            log.info("线程1成功获取两把锁");
                            lock2.unlock();
                            lock1.unlock();
                            break;
                        } else {
                            log.info("线程1尝试获取lock1失败， 已重试");
                            lock1.unlock();
                            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
                        }

                    } else {
                        log.info("线程1获取lock1失败，已重试");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            if (flag == 2) {
                try {
                    if (lock2.tryLock(3000, TimeUnit.MILLISECONDS)) {
                        TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
                        log.info("线程2获得lock1");
                        if (lock1.tryLock(3000, TimeUnit.MILLISECONDS)) {
                            log.info("线程2成功获取两把锁");
                            lock1.unlock();
                            lock2.unlock();
                            break;
                        } else {
                            log.info("线程2尝试获取lock1失败， 已重试");
                            lock2.unlock();
                            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
                        }

                    } else {
                        log.info("线程2获取lock2失败，已重试");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
