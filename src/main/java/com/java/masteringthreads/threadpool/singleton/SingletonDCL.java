package com.java.masteringthreads.threadpool.singleton;
/*
* singleton with double check lock
* */
public class SingletonDCL {
    private static SingletonDCL singletonDCL;
    private final boolean initialized;

    public SingletonDCL() {
        this.initialized = true;
        System.out.println(getClass() + " initialized");
    }

    public boolean getInitialized() {
        return initialized;
    }

    public synchronized static SingletonDCL getSingletonDCL() {
        if (singletonDCL == null) {
            synchronized (SingletonDCL.class) {
                if (singletonDCL == null) {  // double check locking
                    return new SingletonDCL();
                }
            }
        }
        return singletonDCL;
    }
}
