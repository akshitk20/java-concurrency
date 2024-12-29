package com.java.masteringthreads.threadpool.blockingqueue;

import com.java.masteringthreads.threadpool.ThreadPool;
import com.java.masteringthreads.threadpool.threadpoolex.ThreadPoolEx;

import java.util.concurrent.TimeUnit;

/*
* Use blocking queue to simplify thread pool
* replace our task queue with LinkedBlockingQueue and add AtomicBoolean running
* to state whether the pool has been shutdown or not. The reason that we need a
* separate field for the state is that we cannot lock the queue for exclusive access.
* We will not need Reentrant lock and Condition once we move to LinkedBlockingQueue
*
* */
public interface ThreadPoolBlocking extends ThreadPoolEx {
    /*
    * use countdown latch to keep track of live worker threads
    * */
    public boolean awaitTermination(long time, TimeUnit unit) throws InterruptedException;
}
