package com.java.masteringthreads.threadpool.singleton;

public class SingletonLoader {
    private final boolean initialized;

    // let the class loader manage the thread safety for us.
    private static class Loader {
        private static final SingletonLoader singletonLoader = new SingletonLoader();
    }
    public SingletonLoader() {
        this.initialized = true;
    }

    public boolean getInitialized() {
        return initialized;
    }
    public static SingletonLoader getSingletonLoader() {
        return Loader.singletonLoader;
    }
}
