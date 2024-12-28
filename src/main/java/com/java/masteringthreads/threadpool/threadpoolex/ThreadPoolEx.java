package com.java.masteringthreads.threadpool.threadpoolex;

import com.java.masteringthreads.threadpool.ThreadPool;

import java.util.List;

public interface ThreadPoolEx extends ThreadPool {

    /*
    * shuts down the threadpool abruptly interrupting the worker threads
    * currently executing tasks, and returning a list of unstarted tasks.
    * */
    List<Runnable> shutDownNow();
}
