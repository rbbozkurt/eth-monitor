package com.rbbozkurt.ethmonitor.cache;

import com.github.benmanes.caffeine.cache.Cache;

import java.util.function.Function;

/**
 * A Caffeine-based implementation of the {@link CacheLayer} interface.
 *
 * @param <K> the type of keys used for cache entries
 * @param <V> the type of values stored in the cache
 */
public class CaffeineCacheLayer<K, V> implements CacheLayer<K, V> {

    private final Cache<K, V> cache;

    /**
     * Constructs a new Caffeine-backed cache layer.
     *
     * @param cache the underlying Caffeine cache
     */
    public CaffeineCacheLayer(Cache<K, V> cache) {
        this.cache = cache;
    }

    /**
     * Retrieves the value associated with the specified key, or {@code null} if not present.
     *
     * @param key the key to look up in the cache
     * @return the cached value, or {@code null} if not present
     */
    @Override
    public V get(K key) {
        return cache.getIfPresent(key);
    }

    /**
     * Retrieves the value associated with the specified key, or computes it
     * using the provided loader if not already cached.
     *
     * @param key the key to look up or compute
     * @param loader a function to compute the value if absent
     * @return the cached or newly computed value
     */
    @Override
    public V getOrCompute(K key, Function<K, V> loader) {
        return cache.get(key, loader);
    }

    /**
     * Stores the specified value under the given key in the cache.
     *
     * @param key the key to associate with the value
     * @param value the value to cache
     */
    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    /**
     * Removes the entry associated with the specified key from the cache.
     *
     * @param key the key to invalidate
     */
    @Override
    public void invalidate(K key) {
        cache.invalidate(key);
    }

    /**
     * Clears all entries from the cache.
     */
    @Override
    public void clear() {
        cache.invalidateAll();
    }
}
