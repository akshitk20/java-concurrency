package com.java.masteringthreads.threadpool.threadcontention;

import java.util.concurrent.atomic.AtomicLong;

public class RaceConditionDemo implements Runnable{

    //private int count = 0;
    private final AtomicLong count = new AtomicLong();
    @Override
    public void run() {
        for (int i = 0 ; i < 100_000_000 ; i++) {
            count.getAndIncrement();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        RaceConditionDemo r = new RaceConditionDemo();
//        Thread t1 = new Thread(r);
//        Thread t2 = new Thread(r);
//
//        t1.start();
//        t2.start();
        Thread t1 = Thread.ofPlatform().start(r);
        Thread t2 = Thread.ofPlatform().start(r);
        t1.join();
        t2.join();
        System.out.println("count is : " +r.count);
    }


}
