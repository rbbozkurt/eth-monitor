package com.rbbozkurt.ethmonitor.cache;

import java.util.function.Function;

/**
 * A generic caching interface that defines standard cache operations.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
public interface CacheLayer<K, V> {

    /**
     * Retrieves the cached value associated with the specified key.
     *
     * @param key the key whose associated value is to be returned
     * @return the cached value, or {@code null} if not present
     */
    V get(K key);

    /**
     * Retrieves the value associated with the specified key if present;
     * otherwise computes it using the provided loader function, caches it,
     * and returns the computed value.
     *
     * @param key the key whose associated value is to be returned or computed
     * @param loader a function to compute the value if not present in the cache
     * @return the cached or newly computed value
     */
    V getOrCompute(K key, Function<K, V> loader);

    /**
     * Caches the specified value with the associated key.
     *
     * @param key the key with which the specified value is to be associated
     * @param value the value to be cached
     */
    void put(K key, V value);

    /**
     * Removes the cached value associated with the specified key, if present.
     *
     * @param key the key whose cached value is to be removed
     */
    void invalidate(K key);

    /**
     * Clears all entries from the cache.
     */
    void clear();
}
