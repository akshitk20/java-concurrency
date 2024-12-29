package com.java.masteringthreads.threadpool.excerise.draw;

public class HouseDrawing extends StupidFramework {
    private final String color;
    private static final ThreadLocal<String> tempColor = new ThreadLocal<>();

    public HouseDrawing(String title, String color) {
        super(saveColor(title, color));
        this.color = color;
        tempColor.remove();
    }

    private static String saveColor(String title, String color) {
        tempColor.set(color);
        return title;
    }

    @Override
    public void draw() {
        System.out.println("Drawing house with color " + getColor());
    }

    private String getColor() {
        if (color == null) return tempColor.get();
        return color;
    }
}
