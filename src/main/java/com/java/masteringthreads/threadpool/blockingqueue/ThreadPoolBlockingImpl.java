package com.java.masteringthreads.threadpool.blockingqueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadPoolBlockingImpl implements ThreadPoolBlocking {
    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Runnable POISON_PILL = () -> {};
    private final CountDownLatch countDownLatch;

    private final List<Worker> workers;

    public ThreadPoolBlockingImpl(int size) {
        if (size < 1) throw new IllegalArgumentException("size cannot be less than 1");
        List<Worker> tempWorkers = new ArrayList<>();
        countDownLatch = new CountDownLatch(size);
        for (int i = 0 ; i < size ; i++) {
            Worker worker = new Worker("Worker " + i);
            worker.start();
            tempWorkers.add(worker);
        }
        workers = List.copyOf(tempWorkers);
    }

    public Runnable take() throws InterruptedException {
        Runnable task = queue.take();
        if (task == POISON_PILL) {
            queue.add(POISON_PILL);
            //running.getAndSet(true);
        }
        return task;
    }

    @Override
    public void submit(Runnable task) {
        //if (task == POISON_PILL) throw new RejectedExecutionException("Shutdown has happened");
        if (!running.get()) throw new RejectedExecutionException("shutdown");
        queue.add(task);
        //running.getAndSet(true);
    }

    @Override
    public int getRunQueueLength() {
        return (int) queue.stream()
                .filter(r -> r != POISON_PILL)
                .count();
    }

    @Override
    public void shutdown() {
        if (running.compareAndSet(true, false)) {
            queue.add(POISON_PILL);
        }
    }

    @Override
    public boolean awaitTermination(long time, TimeUnit timeUnit) throws InterruptedException {
        return countDownLatch.await(time, timeUnit);
    }

    @Override
    public List<Runnable> shutDownNow() {
        shutdown();
        List<Runnable> tasks = queue.stream()
                        .filter(task -> task != POISON_PILL)
                        .toList();
        queue.removeIf(task -> task == POISON_PILL);
        if (queue.isEmpty()) submit(POISON_PILL);
        workers.forEach(Thread::interrupt);
        System.out.println("shutdown completed returning task length " + tasks.size());
        return tasks;
    }

    private class Worker extends Thread {
        public Worker(String name) {
            super(name);
        }

        @Override
        public void run() {
            while(true) {
                try {
                    var task = take();
                    if (task == POISON_PILL) {
                        countDownLatch.countDown();
                        break;
                    }
                    task.run();
                } catch (InterruptedException e) {
                    throw new RejectedExecutionException(e);
                }
            }
            System.out.println("Completed Thread " + getName());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolBlocking threadPool = new ThreadPoolBlockingImpl(5);
        threadPool.submit(() -> System.out.println("Task1"));
        threadPool.submit(() -> System.out.println("Task2"));
        threadPool.submit(() -> System.out.println("Task3"));
//        List<Runnable> runnables = threadPool.shutDownNow();
//        runnables.forEach(System.out::println);
        threadPool.submit(() -> System.out.println("Task4"));
        threadPool.submit(() -> System.out.println("Task5"));
        threadPool.awaitTermination(4, TimeUnit.MILLISECONDS);
        threadPool.shutdown();
    }
}
