package com.java.masteringthreads.threadpool.practical.airportbaggage.approach1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConveyerBelt {

    private final BlockingQueue<Baggage> queue = new LinkedBlockingQueue<>();

    public void addBaggage(Baggage baggage) throws InterruptedException {
        queue.put(baggage);
    }

    public Baggage getBaggage() throws InterruptedException {
        return queue.take();
    }
}
