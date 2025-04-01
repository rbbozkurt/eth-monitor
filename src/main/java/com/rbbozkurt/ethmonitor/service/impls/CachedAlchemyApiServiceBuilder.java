package com.rbbozkurt.ethmonitor.service.impls;

import com.rbbozkurt.ethmonitor.cache.CacheLayer;
import com.rbbozkurt.ethmonitor.cache.CacheLayerFactory;
import com.rbbozkurt.ethmonitor.client.interfaces.BalancesAPI;
import com.rbbozkurt.ethmonitor.client.interfaces.PricesAPI;
import com.rbbozkurt.ethmonitor.client.interfaces.TokenAPI;
import com.rbbozkurt.ethmonitor.client.interfaces.TransfersAPI;
import com.rbbozkurt.ethmonitor.dto.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Builder class for constructing instances of {@link CachedAlchemyApiService} with all required APIs and cache layers.
 * The builder ensures that all necessary dependencies are provided before creating the final {@link CachedAlchemyApiService}.
 */
public class CachedAlchemyApiServiceBuilder {

    private static final Logger logger = Logger.getLogger(CachedAlchemyApiServiceBuilder.class.getName());

    private BalancesAPI balancesAPI;
    private PricesAPI pricesAPI;
    private TokenAPI tokenAPI;
    private TransfersAPI transfersAPI;

    private CacheLayer<String, BalanceResponse> balancesApiCache;
    private CacheLayer<String, EthBalanceResponse> ethBalanceCache;
    private CacheLayer<String, TokenPriceResponse> priceCache;
    private CacheLayer<String, TokenMetadataResponse> tokenCache;
    private CacheLayer<String, TransferResponse> transferCache;

    private ExecutorService executor;

    /**
     * Sets the {@link BalancesAPI} for the builder.
     *
     * @param api the BalancesAPI
     * @return the builder instance for method chaining
     */
    public CachedAlchemyApiServiceBuilder withBalancesAPI(BalancesAPI api) {
        this.balancesAPI = api;
        return this;
    }

    /**
     * Sets the {@link PricesAPI} for the builder.
     *
     * @param api the PricesAPI
     * @return the builder instance for method chaining
     */
    public CachedAlchemyApiServiceBuilder withPricesAPI(PricesAPI api) {
        this.pricesAPI = api;
        return this;
    }

    /**
     * Sets the {@link TokenAPI} for the builder.
     *
     * @param api the TokenAPI
     * @return the builder instance for method chaining
     */
    public CachedAlchemyApiServiceBuilder withTokenAPI(TokenAPI api) {
        this.tokenAPI = api;
        return this;
    }

    /**
     * Sets the {@link TransfersAPI} for the builder.
     *
     * @param api the TransfersAPI
     * @return the builder instance for method chaining
     */
    public CachedAlchemyApiServiceBuilder withTransfersAPI(TransfersAPI api) {
        this.transfersAPI = api;
        return this;
    }

    /**
     * Sets the cache layer for token balances.
     *
     * @param cache the cache layer for token balances
     * @return the builder instance for method chaining
     */
    public CachedAlchemyApiServiceBuilder withBalancesCache(CacheLayer<String, BalanceResponse> cache) {
        this.balancesApiCache = cache;
        return this;
    }

    /**
     * Sets the cache layer for ETH balances.
     *
     * @param cache the cache layer for ETH balances
     * @return the builder instance for method chaining
     */
    public CachedAlchemyApiServiceBuilder withEthBalanceCache(CacheLayer<String, EthBalanceResponse> cache) {
        this.ethBalanceCache = cache;
        return this;
    }

    /**
     * Sets the cache layer for token prices.
     *
     * @param cache the cache layer for token prices
     * @return the builder instance for method chaining
     */
    public CachedAlchemyApiServiceBuilder withPriceCache(CacheLayer<String, TokenPriceResponse> cache) {
        this.priceCache = cache;
        return this;
    }

    /**
     * Sets the cache layer for token metadata.
     *
     * @param cache the cache layer for token metadata
     * @return the builder instance for method chaining
     */
    public CachedAlchemyApiServiceBuilder withTokenCache(CacheLayer<String, TokenMetadataResponse> cache) {
        this.tokenCache = cache;
        return this;
    }

    /**
     * Sets the cache layer for transfer responses.
     *
     * @param cache the cache layer for transfer responses
     * @return the builder instance for method chaining
     */
    public CachedAlchemyApiServiceBuilder withTransferCache(CacheLayer<String, TransferResponse> cache) {
        this.transferCache = cache;
        return this;
    }

    /**
     * Sets the executor service for asynchronous operations.
     *
     * @param executor the executor service
     * @return the builder instance for method chaining
     */
    public CachedAlchemyApiServiceBuilder withExecutor(ExecutorService executor) {
        this.executor = executor;
        return this;
    }

    /**
     * Builds and returns an instance of {@link CachedAlchemyApiService}.
     * This method ensures that all necessary dependencies are provided.
     *
     * @return an instance of {@link CachedAlchemyApiService}
     * @throws IllegalStateException if any required dependencies are not set
     */
    public CachedAlchemyApiService build() {

        // Ensure required services are provided
        ensureRequired(balancesAPI, "BalancesAPI");
        ensureRequired(pricesAPI, "PricesAPI");
        ensureRequired(tokenAPI, "TokenAPI");
        ensureRequired(transfersAPI, "TransfersAPI");

        // Ensure caches are provided or created
        CacheLayerFactory factory = CacheLayerFactory.getInstance();

        if (balancesApiCache == null) {
            balancesApiCache = factory.createBalancesCache();
        }
        if (ethBalanceCache == null) {
            ethBalanceCache = factory.createEthBalanceCache();
        }
        if (priceCache == null) {
            priceCache = factory.createPricesCache();
        }
        if (tokenCache == null) {
            tokenCache = factory.createTokenCache();
        }
        if (transferCache == null) {
            transferCache = factory.createTransferResponseCache();
        }
        if (executor == null) {
            executor = Executors.newVirtualThreadPerTaskExecutor();
        }

        // Build and return the CachedAlchemyApiService
        CachedAlchemyApiService service = new CachedAlchemyApiService(
                balancesAPI,
                pricesAPI,
                tokenAPI,
                transfersAPI,
                balancesApiCache,
                ethBalanceCache,
                priceCache,
                tokenCache,
                transferCache
        );
        return service;
    }

    /**
     * Ensures that the required fields are not null.
     *
     * @param field the field to check
     * @param name the name of the field
     * @throws IllegalStateException if the field is null
     */
    private void ensureRequired(Object field, String name) {
        if (field == null) {
            throw new IllegalStateException(name + " must be provided.");
        }
    }
}
