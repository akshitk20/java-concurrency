package com.java.masteringthreads.virtualthreads;

/*
* virtual threads needs to be stored in a variable and join needs to be called which
* waits for a thread to finish. if no join is called nothing will be printed since virtual threads
* run on carrier threads which runs in background
* */
public class HelloWorld {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = Thread.ofVirtual()
                .start(() -> System.out.println("Hello world"));
        System.out.println("Thread : " +thread.getClass());
        thread.join();
    }
}
