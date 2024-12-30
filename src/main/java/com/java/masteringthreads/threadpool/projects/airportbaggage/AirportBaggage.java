package com.java.masteringthreads.threadpool.projects.airportbaggage;

public interface AirportBaggage {
    void submit(AirportBaggageImpl.Baggage task);
    void shutdown();
}
