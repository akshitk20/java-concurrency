package com.java.masteringthreads.threadpool.projects.airportbaggage.approach1;

public class Handler implements Runnable {
    private final ConveyerBelt conveyerBelt;

    public Handler(ConveyerBelt conveyerBelt) {
        this.conveyerBelt = conveyerBelt;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Baggage baggage = conveyerBelt.getBaggage();
                System.out.println("Processing Baggage with id : " +baggage.id() + "with thread " +Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
