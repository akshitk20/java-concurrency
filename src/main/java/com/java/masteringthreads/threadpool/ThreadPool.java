package com.java.masteringthreads.threadpool;

public interface ThreadPool {
    /*
    * submit a task to the thread pool where it will be placed in the queue and picked by
    * available thread.
    * */
    void submit(Runnable task);

    /*
    * {@return unstarted threads}
    * */
    int getRunQueueLength();

    /*
    * shuts down the thread pool allowing the tasks that have been submitted to complete
    * no furthur task can be submitted. After the task are completed the thread needs to terminate themselves
    * */
    void shutdown();
}
