package com.java.masteringthreads.threadpool.practical.restaurantordermanager;

public interface RestaurantManager {
    void submit(Order order);
    void shutdown();
}
