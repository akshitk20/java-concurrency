package com.java.masteringthreads.threadpool.cacheholder;

import java.util.HashMap;
import java.util.Map;

// threadsafe - CacheHolder2 is immutable
public class CacheHolder2 {

    private final Map<String, Object> map;

    public CacheHolder2(Map<String, Object> map) {
        this.map = new HashMap<>(map);
    }

    public <E> E get(String key) {
        return (E) this.map.get(key);
    }
}
