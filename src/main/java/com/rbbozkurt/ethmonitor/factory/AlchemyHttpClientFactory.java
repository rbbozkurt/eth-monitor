package com.rbbozkurt.ethmonitor.factory;

import com.rbbozkurt.ethmonitor.client.impls.AlchemyHttpClient;
import com.rbbozkurt.ethmonitor.client.interfaces.HttpClient;

import java.util.concurrent.ConcurrentHashMap;

/**
 * A factory class for creating and caching instances of {@link HttpClient}.
 * This class ensures that only one instance of {@link AlchemyHttpClient} is created for each unique base URL.
 */
public class AlchemyHttpClientFactory implements HttpClientFactory {

    private static final AlchemyHttpClientFactory INSTANCE = new AlchemyHttpClientFactory();

    /**
     * Returns the singleton instance of the {@link AlchemyHttpClientFactory}.
     *
     * @return the singleton instance
     */
    public static AlchemyHttpClientFactory getInstance() {
        return INSTANCE;
    }

    // Cache to store and reuse HttpClient instances based on their base URL
    private final ConcurrentHashMap<String, HttpClient> cache = new ConcurrentHashMap<>();

    /**
     * Private constructor to prevent direct instantiation.
     */
    private AlchemyHttpClientFactory() {
    }

    /**
     * Retrieves an {@link HttpClient} for the specified base URL.
     * If a client for the given URL already exists in the cache, it will be returned.
     * Otherwise, a new {@link AlchemyHttpClient} will be created and added to the cache.
     *
     * @param baseUrl the base URL for the {@link HttpClient}
     * @return an {@link HttpClient} instance
     */
    @Override
    public HttpClient getClient(String baseUrl) {
        return cache.computeIfAbsent(baseUrl, AlchemyHttpClient::new);
    }
}
