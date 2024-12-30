package com.java.masteringthreads.threadpool.cacheholder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// threadsafe - immutable
public class CacheHolder4 {
    private final Map<String, Object> map;

    public CacheHolder4(Map<String, Object> map) {
        this.map = new ConcurrentHashMap<>(map);
    }

    public <E> E get(String key) {
        return (E) map.get(key);
    }

}
