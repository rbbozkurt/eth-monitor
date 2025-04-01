package com.rbbozkurt.ethmonitor.factory;

import com.rbbozkurt.ethmonitor.cache.CacheLayer;
import com.rbbozkurt.ethmonitor.cache.CacheLayerFactory;
import com.rbbozkurt.ethmonitor.client.interfaces.BalancesAPI;
import com.rbbozkurt.ethmonitor.client.interfaces.PricesAPI;
import com.rbbozkurt.ethmonitor.client.interfaces.TokenAPI;
import com.rbbozkurt.ethmonitor.client.interfaces.TransfersAPI;
import com.rbbozkurt.ethmonitor.dto.*;
import com.rbbozkurt.ethmonitor.model.WalletAnalysisReport.HistoricalTransfer;
import com.rbbozkurt.ethmonitor.model.WalletAnalysisReport.TokenBalance;
import com.rbbozkurt.ethmonitor.factory.ServiceFactory;
import com.rbbozkurt.ethmonitor.service.impls.*;
import com.rbbozkurt.ethmonitor.service.interfaces.ApiService;
import com.rbbozkurt.ethmonitor.service.interfaces.BalanceService;
import com.rbbozkurt.ethmonitor.service.interfaces.SwapDetectorService;
import com.rbbozkurt.ethmonitor.service.interfaces.TransferService;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A factory class for creating instances of services such as {@link TransferService}, {@link BalanceService},
 * {@link ApiService}, and {@link SwapDetectorService}. It uses caching to optimize the creation of services
 * with the same API keys, and ensures the reuse of previously created service instances.
 */
public class AlchemyServiceFactory implements ServiceFactory {

    private static final AlchemyServiceFactory INSTANCE = new AlchemyServiceFactory();

    /**
     * Returns the singleton instance of the {@link AlchemyServiceFactory}.
     *
     * @return the singleton instance
     */
    public static AlchemyServiceFactory getInstance() {
        return INSTANCE;
    }

    private final AlchemyAPIFactory apiFactory = AlchemyAPIFactory.getInstance();
    private final CacheLayerFactory cacheLayerFactory = CacheLayerFactory.getInstance();

    // Caches for different service types, ensuring reusability for the same keys
    private final ConcurrentHashMap<String, TransferService> transferCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BalanceService> balanceCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ApiService> apiServiceCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, SwapDetectorService> swapDetectorCache = new ConcurrentHashMap<>();

    /**
     * Private constructor to prevent direct instantiation. This ensures that only the singleton instance
     * of {@link AlchemyServiceFactory} is used throughout the application.
     */
    private AlchemyServiceFactory() {}

    /**
     * Creates or retrieves a cached instance of the {@link TransferService} for the given API keys.
     *
     * @param balancesApiKey the API key for balances
     * @param pricesApiKey the API key for prices
     * @param tokensApiKey the API key for tokens
     * @param transfersApiKey the API key for transfers
     * @return a {@link TransferService} instance
     */
    @Override
    public TransferService getTransferService(String balancesApiKey, String pricesApiKey, String tokensApiKey, String transfersApiKey) {
        String key = generateCompositeKey(balancesApiKey, pricesApiKey, tokensApiKey, transfersApiKey);
        return transferCache.computeIfAbsent(key, k -> {
            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
            return new AlchemyTransferService(getApiService(balancesApiKey, pricesApiKey, tokensApiKey, transfersApiKey), executor);
        });
    }

    /**
     * Creates or retrieves a cached instance of the {@link ApiService} for the given API keys.
     *
     * @param balancesApiKey the API key for balances
     * @param pricesApiKey the API key for prices
     * @param tokensApiKey the API key for tokens
     * @param transfersApiKey the API key for transfers
     * @return an {@link ApiService} instance
     */
    @Override
    public ApiService getApiService(String balancesApiKey, String pricesApiKey, String tokensApiKey, String transfersApiKey) {
        String key = generateCompositeKey(balancesApiKey, pricesApiKey, tokensApiKey, transfersApiKey);

        return apiServiceCache.computeIfAbsent(key, k -> {
            BalancesAPI balancesAPI = apiFactory.createBalancesAPI(balancesApiKey);
            PricesAPI pricesAPI = apiFactory.createPricesAPI(pricesApiKey);
            TokenAPI tokenAPI = apiFactory.createTokenAPI(tokensApiKey);
            TransfersAPI transfersAPI = apiFactory.createTransfersAPI(transfersApiKey);

            return new CachedAlchemyApiServiceBuilder()
                    .withBalancesAPI(balancesAPI)
                    .withPricesAPI(pricesAPI)
                    .withTokenAPI(tokenAPI)
                    .withTransfersAPI(transfersAPI)
                    .build();
        });
    }

    /**
     * Creates or retrieves a cached instance of the {@link BalanceService} for the given API keys.
     *
     * @param balancesApiKey the API key for balances
     * @param pricesApiKey the API key for prices
     * @param tokensApiKey the API key for tokens
     * @param transfersApiKey the API key for transfers
     * @return a {@link BalanceService} instance
     */
    @Override
    public BalanceService getBalanceService(String balancesApiKey, String pricesApiKey, String tokensApiKey, String transfersApiKey) {
        String key = generateCompositeKey(balancesApiKey, pricesApiKey, tokensApiKey, transfersApiKey);

        return balanceCache.computeIfAbsent(key, k -> {
            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
            return new AlchemyBalanceService(
                    getApiService(balancesApiKey, pricesApiKey, tokensApiKey, transfersApiKey),
                    executor
            );
        });
    }

    /**
     * Creates or retrieves a cached instance of the {@link SwapDetectorService}.
     *
     * @return a {@link SwapDetectorService} instance
     */
    @Override
    public SwapDetectorService getSwapDetectorService() {
        return swapDetectorCache.computeIfAbsent("detector", key -> new BasicSwapDetectorService());
    }

    /**
     * Generates a composite key based on the given API keys, used for caching service instances.
     *
     * @param keys the API keys to generate a unique composite key
     * @return a composite key string
     */
    private String generateCompositeKey(String... keys) {
        return String.join("::", keys);
    }
}
