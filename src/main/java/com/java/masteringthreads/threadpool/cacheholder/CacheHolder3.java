package com.java.masteringthreads.threadpool.cacheholder;

import java.util.Map;

// threadsafe and immutable
public class CacheHolder3 {
    private final Map<String, Object> map;

    public CacheHolder3(Map<String, Object> map) {
        this.map = Map.copyOf(map);
    }

    public <E> E get(String key) {
        return (E) map.get(key);
    }
}
