package com.github.jsiu93.synchronize;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Xiao Zhuhua
 * @date 2020/1/7 10:00 AM
 * @since 1.0.0
 */
@Slf4j
public class TwoThreads4OddEvenSync {


    private static int count;
    private static Object lock = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            while (count <= 100) {
                synchronized (lock) {
                    if ((count & 1) == 1) {
                        log.info("{}", count++);
                    }
                }
            }
        }).start();


        new Thread(() -> {
            while (count <= 100) {
                synchronized (lock) {
                    if ((count & 1) == 0) {
                        log.info("{}", count++);
                    }

                }

            }

        }).start();
    }


}
