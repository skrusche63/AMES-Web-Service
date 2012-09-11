package de.kp.ames.web.core.util;

import java.util.LinkedHashMap;
import java.util.Map;
public class LRUCache<K,V> extends LinkedHashMap<K,V> {
    
	private static final long serialVersionUID = 2561369090200082845L;
	private int max_cap;

    public LRUCache(int initial_cap, int max_cap) {
        super(initial_cap, 0.75f, true);
        this.max_cap = max_cap;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size() > this.max_cap;
    }
}