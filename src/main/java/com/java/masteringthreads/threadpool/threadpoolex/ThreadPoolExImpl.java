package com.java.masteringthreads.threadpool.threadpoolex;

import com.java.masteringthreads.threadpool.ThreadPool;
import com.java.masteringthreads.threadpool.monitor.ThreadPoolImpl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ThreadPoolExImpl implements ThreadPoolEx {

    private final Deque<Runnable> queue = new ArrayDeque<>();
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Runnable POISON_PILL = () -> {};
    private final List<Worker> workers;

    public ThreadPoolExImpl(int size) {
        if (size < 1) throw new IllegalArgumentException("Size of pool cannot be less than 1");
        List<Worker> tempWorkers = new ArrayList<>();
        for (int i = 0 ; i < size ; i++) {
            Worker worker = new Worker("Worker " + i);
            worker.start();
            tempWorkers.add(worker);
        }
        workers = List.copyOf(tempWorkers);
    }

    private Runnable take() {
        lock.lock();
        try {
            while (queue.isEmpty()) notEmpty.awaitUninterruptibly();
            var task = queue.removeFirst();
            if (task == POISON_PILL) {
                queue.addLast(task);
                notEmpty.signal();
            }
            return task;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void submit(Runnable task) {
        lock.lock();
        try {
            if (queue.peekLast() == POISON_PILL) {
                throw new RejectedExecutionException("shutdown");
            }
            queue.addLast(task);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getRunQueueLength() {
        lock.lock();
        try {
            return (int) queue.stream()
                    .filter(task -> task != POISON_PILL)
                    .count();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void shutdown() {
        lock.lock();
        try {
            if (queue.peekLast() != POISON_PILL) {
                submit(POISON_PILL);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Runnable> shutDownNow() {
        lock.lock();
        try {
            shutdown();
            List<Runnable> tasks = new ArrayList<>();
            while (!queue.isEmpty()) {
                if (queue.removeFirst() != POISON_PILL) tasks.add(queue.removeFirst());
            }
            workers.forEach(Thread::interrupt);
            System.out.println("shutdown completed returning task length " + tasks.size());
            return tasks;
        } finally {
            lock.unlock();
        }
    }

    private class Worker extends Thread {
        public Worker(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (true) {
                var task = take();
                if (task == POISON_PILL) {
                    System.out.println("Found a poison pill");
                    break;
                }
                task.run();
            }
            System.out.println("Completed thread " + getName());
        }
    }

    public static void main(String[] args) {
        ThreadPoolEx threadPool = new ThreadPoolExImpl(5);
        threadPool.submit(() -> System.out.println("Task1"));
        threadPool.submit(() -> System.out.println("Task2"));
        threadPool.submit(() -> System.out.println("Task3"));
        List<Runnable> runnables = threadPool.shutDownNow();
        runnables.forEach(System.out::println);
        threadPool.submit(() -> System.out.println("Task4"));
        threadPool.submit(() -> System.out.println("Task5"));
        threadPool.shutdown();
    }

}
