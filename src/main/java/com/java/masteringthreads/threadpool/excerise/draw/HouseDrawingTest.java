package com.java.masteringthreads.threadpool.excerise.draw;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class HouseDrawingTest {
    public static void main(String[] args) {
        Phaser phaser = new Phaser(4);

        class SuspendedHouseDrawing extends HouseDrawing {

            public SuspendedHouseDrawing(String title, String color) {
                super(title, color);
            }

            @Override
            public void draw() {
                phaser.arriveAndAwaitAdvance();
                super.draw();
            }
        }

        try (ExecutorService pool = Executors.newCachedThreadPool()) {
            pool.submit(() -> new SuspendedHouseDrawing("HomeSweetHome1", "beige").draw());
            pool.submit(() -> new SuspendedHouseDrawing("HomeSweetHome2", "red").draw());
            pool.submit(() -> new SuspendedHouseDrawing("HomeSweetHome3", "green").draw());
            pool.submit(() -> new SuspendedHouseDrawing("HomeSweetHome4", "purple").draw());
        }
    }
}