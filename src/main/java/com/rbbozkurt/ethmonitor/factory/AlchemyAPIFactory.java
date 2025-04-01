package com.rbbozkurt.ethmonitor.factory;

import com.rbbozkurt.ethmonitor.cache.CacheLayer;
import com.rbbozkurt.ethmonitor.cache.CaffeineCacheLayerBuilder;
import com.rbbozkurt.ethmonitor.client.impls.*;
import com.rbbozkurt.ethmonitor.client.interfaces.*;
import com.rbbozkurt.ethmonitor.dto.*;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A factory class for creating instances of Alchemy API clients.
 * This factory provides methods for creating API clients for Prices, Token, Transfers, and Balances,
 * and caches the created instances to optimize performance and reduce the number of new objects created.
 */
public class AlchemyAPIFactory implements APIFactory {

    private static final AlchemyAPIFactory INSTANCE = new AlchemyAPIFactory();

    private final HttpClientFactory httpClientFactory;
    private final ConcurrentHashMap<String, PricesAPI> pricesApiCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, TokenAPI> tokenApiCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, TransfersAPI> transfersApiCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BalancesAPI> balancesApiCache = new ConcurrentHashMap<>();

    /**
     * Private constructor for initializing the factory.
     * Initializes the {@link HttpClientFactory} instance used to create HTTP clients.
     */
    private AlchemyAPIFactory() {
        this.httpClientFactory = AlchemyHttpClientFactory.getInstance();
    }

    /**
     * Returns the singleton instance of the AlchemyAPIFactory.
     *
     * @return the {@link AlchemyAPIFactory} instance
     */
    public static AlchemyAPIFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Creates or retrieves a cached instance of the {@link PricesAPI} for the given API key.
     * Caches the instance for subsequent use.
     *
     * @param apiKey the Alchemy API key
     * @return a {@link PricesAPI} instance
     */
    @Override
    public PricesAPI createPricesAPI(String apiKey) {
        return pricesApiCache.computeIfAbsent(apiKey, key -> {
            return new AlchemyPricesAPI(apiKey, httpClientFactory);
        });
    }

    /**
     * Creates or retrieves a cached instance of the {@link TokenAPI} for the given API key.
     * Caches the instance for subsequent use.
     *
     * @param apiKey the Alchemy API key
     * @return a {@link TokenAPI} instance
     */
    @Override
    public TokenAPI createTokenAPI(String apiKey) {
        return tokenApiCache.computeIfAbsent(apiKey, key -> {
            return new AlchemyTokenAPI(key, httpClientFactory);
        });
    }

    /**
     * Creates or retrieves a cached instance of the {@link TransfersAPI} for the given API key.
     * Caches the instance for subsequent use.
     *
     * @param apiKey the Alchemy API key
     * @return a {@link TransfersAPI} instance
     */
    @Override
    public TransfersAPI createTransfersAPI(String apiKey) {
        return transfersApiCache.computeIfAbsent(apiKey, key -> {
            return new AlchemyTransfersAPI(key, httpClientFactory);
        });
    }

    /**
     * Creates or retrieves a cached instance of the {@link BalancesAPI} for the given API key.
     * Caches the instance for subsequent use.
     *
     * @param apiKey the Alchemy API key
     * @return a {@link BalancesAPI} instance
     */
    @Override
    public BalancesAPI createBalancesAPI(String apiKey) {
        return balancesApiCache.computeIfAbsent(apiKey, key -> {
            return new AlchemyBalancesAPI(apiKey, httpClientFactory);
        });
    }
}
