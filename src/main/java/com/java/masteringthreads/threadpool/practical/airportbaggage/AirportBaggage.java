package com.java.masteringthreads.threadpool.practical.airportbaggage;

public interface AirportBaggage {
    void submit(AirportBaggageImpl.Baggage task);
    void shutdown();
}
