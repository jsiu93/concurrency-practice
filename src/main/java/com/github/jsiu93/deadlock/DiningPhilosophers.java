package com.github.jsiu93.deadlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class DiningPhilosophers {

    static class Philosopher implements Runnable{

        private Object leftChopstick;
        private Object rightChopstick;

        public Philosopher(Object leftChopstick, Object rightChopstick) {
            this.leftChopstick = leftChopstick;
            this.rightChopstick = rightChopstick;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    doAction("Thinking");
                    synchronized (leftChopstick) {
                        doAction("Picked up left Chopstick");
                        synchronized (rightChopstick) {
                            doAction("Picked up right Chopstick - eating");
                            log.info("put down right Chopstick");
                        }
                        log.info("put down left Chopstick");
                    }
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }

        }

        private void doAction(String action) throws InterruptedException {
            log.info(action);
            TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 10L));
        }
    }

    public static void main(String[] args) {
        Philosopher[] philosophers = new Philosopher[5];
        Object[] chopsticks = new Object[philosophers.length];
        for (int i = 0; i < chopsticks.length; i++) {
            chopsticks[i] = new Object();
        }

        for (int i = 0; i < philosophers.length; i++) {
            Object leftChopstick = chopsticks[i];
            Object rightChopstick = chopsticks[(i + 1) % chopsticks.length];

            if (i == philosophers.length - 1) {
                philosophers[i] = new Philosopher(rightChopstick, leftChopstick);
            } else {
                philosophers[i] = new Philosopher(leftChopstick, rightChopstick);
            }
            new Thread(philosophers[i], "philosopher No. " + (i + 1)).start();
        }



    }

}
