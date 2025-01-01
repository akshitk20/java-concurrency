package com.java.masteringthreads.virtualthreads;

public class JoinDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread[] thread = new Thread[1000];
        for (int i = 0 ; i < thread.length ; i++) {
            int finalI = i;
            thread[i] = Thread.startVirtualThread(() -> System.out.println("I am thread " + finalI));
        }
        System.out.println("Before join");
        for (Thread thread1: thread) thread1.join();
        System.out.println("After join");
    }
}
