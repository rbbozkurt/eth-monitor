package com.rbbozkurt.ethmonitor.cache;

import com.rbbozkurt.ethmonitor.dto.*;
import com.rbbozkurt.ethmonitor.model.WalletAnalysisReport.HistoricalTransfer;
import com.rbbozkurt.ethmonitor.model.WalletAnalysisReport.TokenBalance;

import java.time.Duration;
import java.util.List;

/**
 * Factory class for creating pre-configured {@link CacheLayer} instances
 * tailored to different data types used in Ethereum monitoring.
 */
public class CacheLayerFactory {

    private static final CacheLayerFactory INSTANCE = new CacheLayerFactory();

    /**
     * Returns the singleton instance of the factory.
     *
     * @return the {@link CacheLayerFactory} instance
     */
    public static CacheLayerFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Creates a cache layer for wallet balances with a TTL of 5 minutes and maximum size of 10,000.
     *
     * @return configured {@link CacheLayer} for {@link BalanceResponse}
     */
    public CacheLayer<String, BalanceResponse> createBalancesCache() {
        return new CaffeineCacheLayerBuilder<String, BalanceResponse>()
                .withMaxSize(10_000)
                .withTTL(Duration.ofMinutes(5))
                .build();
    }

    /**
     * Creates a cache layer for token prices with a TTL of 30 seconds and maximum size of 1,000.
     *
     * @return configured {@link CacheLayer} for {@link TokenPriceResponse}
     */
    public CacheLayer<String, TokenPriceResponse> createPricesCache() {
        return new CaffeineCacheLayerBuilder<String, TokenPriceResponse>()
                .withMaxSize(1_000)
                .withTTL(Duration.ofSeconds(30))
                .build();
    }

    /**
     * Creates a cache layer for token metadata with a TTL of 1 hour and maximum size of 5,000.
     *
     * @return configured {@link CacheLayer} for {@link TokenMetadataResponse}
     */
    public CacheLayer<String, TokenMetadataResponse> createTokenCache() {
        return new CaffeineCacheLayerBuilder<String, TokenMetadataResponse>()
                .withMaxSize(5_000)
                .withTTL(Duration.ofHours(1))
                .build();
    }

    /**
     * Creates a cache layer for ETH balances with a TTL of 2 minutes and maximum size of 2,000.
     *
     * @return configured {@link CacheLayer} for {@link EthBalanceResponse}
     */
    public CacheLayer<String, EthBalanceResponse> createEthBalanceCache() {
        return new CaffeineCacheLayerBuilder<String, EthBalanceResponse>()
                .withMaxSize(2_000)
                .withTTL(Duration.ofMinutes(2))
                .build();
    }

    /**
     * Creates a cache layer for transfer responses with a TTL of 10 minutes and maximum size of 1,000.
     *
     * @return configured {@link CacheLayer} for {@link TransferResponse}
     */
    public CacheLayer<String, TransferResponse> createTransferResponseCache() {
        return new CaffeineCacheLayerBuilder<String, TransferResponse>()
                .withMaxSize(1_000)
                .withTTL(Duration.ofMinutes(10))
                .build();
    }

    /**
     * Creates a cache layer for historical token transfers with a TTL of 10 minutes and maximum size of 1,000.
     *
     * @return configured {@link CacheLayer} for list of {@link HistoricalTransfer}
     */
    public CacheLayer<String, List<HistoricalTransfer>> createHistoricalTransfersCache() {
        return new CaffeineCacheLayerBuilder<String, List<HistoricalTransfer>>()
                .withMaxSize(1_000)
                .withTTL(Duration.ofMinutes(10))
                .build();
    }

    /**
     * Creates a cache layer for token balances with USD valuation with a TTL of 2 minutes and maximum size of 1,000.
     *
     * @return configured {@link CacheLayer} for list of {@link TokenBalance}
     */
    public CacheLayer<String, List<TokenBalance>> createTokenBalancesWithUsdCache() {
        return new CaffeineCacheLayerBuilder<String, List<TokenBalance>>()
                .withMaxSize(1_000)
                .withTTL(Duration.ofMinutes(2))
                .build();
    }

    /**
     * Creates a cache layer for ETH balance with USD valuation with a TTL of 2 minutes and maximum size of 1,000.
     *
     * @return configured {@link CacheLayer} for {@link TokenBalance}
     */
    public CacheLayer<String, TokenBalance> createEthBalanceWithUsdCache() {
        return new CaffeineCacheLayerBuilder<String, TokenBalance>()
                .withMaxSize(1_000)
                .withTTL(Duration.ofMinutes(2))
                .build();
    }
}
