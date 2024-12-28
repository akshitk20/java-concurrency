package com.java.masteringthreads.threadpool.singleton;

public class SingletonSynchronized {
    private static SingletonSynchronized singletonSynchronized;
    private final boolean initialized;

    public SingletonSynchronized() {
        this.initialized = true;
        System.out.println(getClass() + " initialized");
    }

    public boolean getInitialized() {
        return initialized;
    }
    public synchronized static SingletonSynchronized getSingletonSynchronized() {
        if (singletonSynchronized == null) {
            return new SingletonSynchronized();
        }
        return singletonSynchronized;
    }

}
