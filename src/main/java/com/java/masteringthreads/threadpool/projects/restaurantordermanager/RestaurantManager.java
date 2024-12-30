package com.java.masteringthreads.threadpool.projects.restaurantordermanager;

public interface RestaurantManager {
    void submit(Order order);
    void shutdown();
}
