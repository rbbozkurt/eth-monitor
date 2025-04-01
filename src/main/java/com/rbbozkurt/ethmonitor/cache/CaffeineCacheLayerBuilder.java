package com.rbbozkurt.ethmonitor.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

/**
 * A builder class for constructing {@link CaffeineCacheLayer} instances
 * with configurable maximum size and time-to-live (TTL).
 *
 * @param <K> the type of keys used in the cache
 * @param <V> the type of values stored in the cache
 */
public class CaffeineCacheLayerBuilder<K, V> {

    private int maxSize = 1_000;
    private Duration ttl = Duration.ofMinutes(10);

    /**
     * Sets the maximum number of entries the cache may contain.
     *
     * @param maxSize the maximum size of the cache
     * @return the current builder instance
     */
    public CaffeineCacheLayerBuilder<K, V> withMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    /**
     * Sets the time-to-live duration for each cache entry after write.
     *
     * @param ttl the duration after which a cache entry expires
     * @return the current builder instance
     */
    public CaffeineCacheLayerBuilder<K, V> withTTL(Duration ttl) {
        this.ttl = ttl;
        return this;
    }

    /**
     * Builds and returns a {@link CacheLayer} backed by a configured Caffeine cache.
     *
     * @return the configured {@link CacheLayer} instance
     */
    public CacheLayer<K, V> build() {
        Cache<K, V> cache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(ttl)
                .build();

        return new CaffeineCacheLayer<>(cache);
    }
}
