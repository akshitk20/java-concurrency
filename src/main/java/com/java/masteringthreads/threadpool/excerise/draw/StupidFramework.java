package com.java.masteringthreads.threadpool.excerise.draw;

public abstract class StupidFramework {
    public StupidFramework(String title) {
        draw();
        System.out.println("You are using StupidFramework: " + title);
    }

    public abstract void draw();
}
