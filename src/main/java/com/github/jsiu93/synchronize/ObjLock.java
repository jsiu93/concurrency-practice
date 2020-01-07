package com.github.jsiu93.synchronize;

/**
 * @author Xiao Zhuhua
 * @date 2020/1/6 4:01 PM
 * @since 1.0.0
 */
public class ObjLock implements Runnable {

    private int i;
    private Object lock = new Object();

    public ObjLock(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        synchronized (lock) {
            for (int j = 0; j < 10000; j++) {
                i++;
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        ObjLock target = new ObjLock(i);
        Thread t1 = new Thread(target);
        Thread t2 = new Thread(target);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.err.println("over");
        System.err.println(target.i);
    }
}
