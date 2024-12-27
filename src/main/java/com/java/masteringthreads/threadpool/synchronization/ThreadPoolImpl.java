package com.java.masteringthreads.threadpool.synchronization;

import com.java.masteringthreads.threadpool.ThreadPool;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.RejectedExecutionException;

/*
 * implementing Threadpool using synchronization method
 * in the queue we will add in the last and remove from front
 */
public class ThreadPoolImpl implements ThreadPool {

    private final Deque<Runnable> queue = new ArrayDeque<>();

    private final Runnable POISON_PILL = () -> {};

    public ThreadPoolImpl(int size) {
        if (size < 1) throw new IllegalArgumentException("Size of pool cannot be less than 1");
        for (int i = 0 ; i < size ; i++) {
            new Worker("Worker " + i).start();
        }
    }

    private Runnable take() throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) queue.wait();
            var task = queue.removeFirst();
            if (task == POISON_PILL) {
                queue.addLast(task);
                queue.notifyAll();
            }
            return task;
        }
    }
    @Override
    public void submit(Runnable task) {
        synchronized (queue) {
            if (queue.peekLast() == POISON_PILL) {
                throw new RejectedExecutionException("shutdown");
            }
            queue.addLast(task);
            queue.notifyAll();
        }
    }

    @Override
    public int getRunQueueLength() {
        synchronized (queue) {
            return (int) queue.stream()
                    .filter(task -> task != POISON_PILL)
                    .count();
        }
    }

    @Override
    public void shutdown() {
        synchronized (queue) {
            if (queue.peekLast() != POISON_PILL) {
                submit(POISON_PILL);
            }
        }
    }

    private class Worker extends Thread {
        public Worker(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    var task = take();
                    if (task == POISON_PILL) {
                        System.out.println("Found a poison pill");
                        break;
                    }
                    task.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Completed thread " + getName());
        }
    }

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPoolImpl(5);
        threadPool.submit(() -> System.out.println("Task1"));
        threadPool.submit(() -> System.out.println("Task2"));
        threadPool.submit(() -> System.out.println("Task3"));
        threadPool.submit(() -> System.out.println("Task4"));
        threadPool.submit(() -> System.out.println("Task5"));
        threadPool.shutdown();
    }

}
