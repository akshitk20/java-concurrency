package com.java.masteringthreads.virtualthreads;

public class HelloWorld {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = Thread.ofVirtual()
                .start(() -> System.out.println("Hello world"));
        System.out.println("Thread : " +thread.getClass());
        thread.join();
    }
}
