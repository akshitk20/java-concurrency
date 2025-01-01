package com.java.masteringthreads.threadpool.practical.airportbaggage.approach1;

public class AirportBaggageSystem {
    public static void main(String[] args) throws InterruptedException {
        ConveyerBelt conveyerBelt = new ConveyerBelt();
        Handler handler1 = new Handler(conveyerBelt);
        Handler handler2 = new Handler(conveyerBelt);

        Thread t1 = Thread.ofPlatform().start(handler1);
        Thread t2 = Thread.ofPlatform().start(handler2);

        for (int i = 0 ; i < 5 ; i++) {
            conveyerBelt.addBaggage(new Baggage(i, "INDIA"));
        }
    }
}
