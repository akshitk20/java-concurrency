package com.java.masteringthreads.threadpool.singleton;

public class SingletonClassic {
    private static final SingletonClassic singletonClassic = new SingletonClassic();
    private final boolean initialized;

    public SingletonClassic() {
        this.initialized = true;
    }

    public boolean getInitialized() {
        return initialized;
    }
    public static SingletonClassic getSingletonClassic() {
        return singletonClassic;
    }
}
