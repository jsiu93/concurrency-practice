package com.github.jsiu93.concurrency.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Authur: joshuasiu
 * @Date: 2019-06-05 15:32
 * @Description:
 */
@Slf4j
public class CyclicBarrierExample {


    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

    public static void main(String[] args) throws Exception{
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            int finalI = i;
            executorService.execute(() -> {
                try {
                    race(finalI);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }


            });
        }
        executorService.shutdown();

    }


    private static void race(int i) throws Exception {
        Thread.sleep(1000);
        log.info("{} is ready", i);
        cyclicBarrier.await();
        log.info("{} continue", i);

    }
}
