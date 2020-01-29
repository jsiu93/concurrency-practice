package com.github.jsiu93.jmm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldVisibility {

    private  int a = 1;
    private  int b = 2;

    private void change() {
        a = 3;
        b = a;
    }

    private void print() {
        log.info("a={}, b={}", a, b);

    }


    public static void main(String[] args) {
        while (true) {
            FieldVisibility fieldVisibility = new FieldVisibility();
            new Thread(() -> {
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fieldVisibility.change();
            }).start();

            new Thread(() ->{
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fieldVisibility.print();
            }).start();


        }
    }
}
