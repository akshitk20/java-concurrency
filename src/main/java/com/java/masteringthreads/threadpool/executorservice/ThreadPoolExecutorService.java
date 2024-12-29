package com.java.masteringthreads.threadpool.executorservice;

import com.java.masteringthreads.threadpool.blockingqueue.ThreadPoolBlocking;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
* replace the inner working of thread pool with executor service
* */
public class ThreadPoolExecutorService implements ThreadPoolBlocking {
    private final ExecutorService executorService;

    public ThreadPoolExecutorService(int size) {
        this.executorService = Executors.newFixedThreadPool(size);
    }

    @Override
    public void submit(Runnable task) {
        executorService.submit(task);
    }

    @Override
    public int getRunQueueLength() {
        return ((ThreadPoolExecutor) executorService).getQueue().size();
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public boolean awaitTermination(long time, TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(time, unit);
    }

    @Override
    public List<Runnable> shutDownNow() {
        return executorService.shutdownNow();
    }

    public static void main(String[] args) {
        ThreadPoolExecutorService threadPool = new ThreadPoolExecutorService(5);
        threadPool.submit(() -> System.out.println("Task1"));
        threadPool.submit(() -> System.out.println("Task2"));
        threadPool.submit(() -> System.out.println("Task3"));
//        List<Runnable> runnables = threadPool.shutDownNow();
//        runnables.forEach(System.out::println);
        threadPool.submit(() -> System.out.println("Task4"));
        threadPool.submit(() -> System.out.println("Task5"));
        threadPool.shutdown();
    }
}
