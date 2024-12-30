package com.java.masteringthreads.threadpool.cacheholder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//thread safe
public class CacheHolder1 {

    private final Map<String, Object> map = new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public <E> E get(String key) {
        return (E) map.get(key);
    }

}
