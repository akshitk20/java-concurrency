package com.java.masteringthreads.threadpool.projects.airportbaggage;

public interface AirportBaggage {
    void submit(Runnable task);
    void shutdown();
}
