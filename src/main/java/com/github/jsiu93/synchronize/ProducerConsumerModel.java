package com.github.jsiu93.synchronize;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.LinkedList;

/**
 * @author Xiao Zhuhua
 * @date 2020/1/7 10:41 AM
 * @since 1.0.0
 */
public class ProducerConsumerModel {

    public static void main(String[] args) {
        EventStorage eventStorage = new EventStorage();
        Producer producer = new Producer(eventStorage);
        Consumer consumer = new Consumer(eventStorage);

        new Thread(producer).start();
        new Thread(consumer).start();

    }


}

@Slf4j
class EventStorage {
    private int maxSize;
    private LinkedList<Date> storage;


    public EventStorage() {
        this.maxSize = 10;
        this.storage = new LinkedList<>();
    }

    public synchronized void take() {
        if (this.storage.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("拿到:{}, 还剩:{}", this.storage.poll(), this.storage.size());
        notify();
    }

    public synchronized void put() {
        while (this.storage.size() == maxSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.storage.push(new Date());
        log.info("list size:{}", this.storage.size());
        notify();
    }

}

@Slf4j
class Producer implements Runnable {

    private EventStorage eventStorage;

    public Producer(EventStorage eventStorage) {
        this.eventStorage = eventStorage;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            this.eventStorage.put();
        }

    }
}

class Consumer implements Runnable {

    private EventStorage eventStorage;

    public Consumer(EventStorage eventStorage) {
        this.eventStorage = eventStorage;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            this.eventStorage.take();
        }

    }
}