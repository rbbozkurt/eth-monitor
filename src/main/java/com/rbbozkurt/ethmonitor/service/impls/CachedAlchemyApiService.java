package com.rbbozkurt.ethmonitor.service.impls;

import com.rbbozkurt.ethmonitor.cache.CacheLayer;
import com.rbbozkurt.ethmonitor.client.interfaces.BalancesAPI;
import com.rbbozkurt.ethmonitor.client.interfaces.PricesAPI;
import com.rbbozkurt.ethmonitor.client.interfaces.TokenAPI;
import com.rbbozkurt.ethmonitor.client.interfaces.TransfersAPI;
import com.rbbozkurt.ethmonitor.dto.*;
import com.rbbozkurt.ethmonitor.service.interfaces.ApiService;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Cached implementation of the {@link ApiService} that interacts with external APIs for fetching token balances,
 * ETH balances, token metadata, price information, and transfer history. This implementation caches responses
 * to optimize performance and reduce redundant API calls.
 */
public class CachedAlchemyApiService implements ApiService {

    private static final Logger logger = Logger.getLogger(CachedAlchemyApiService.class.getName());

    private final BalancesAPI balancesAPI;
    private final PricesAPI pricesAPI;
    private final TokenAPI tokenAPI;
    private final TransfersAPI transfersAPI;

    private final CacheLayer<String, BalanceResponse> balancesApiCache;
    private final CacheLayer<String, EthBalanceResponse> ethBalanceCache;
    private final CacheLayer<String, TokenPriceResponse> priceCache;
    private final CacheLayer<String, TokenMetadataResponse> tokenCache;
    private final CacheLayer<String, TransferResponse> transferCache;

    /**
     * Constructs a {@link CachedAlchemyApiService} with the given APIs and cache layers.
     *
     * @param balancesAPI the balances API
     * @param pricesAPI the prices API
     * @param tokenAPI the token metadata API
     * @param transfersAPI the transfers API
     * @param balancesApiCache the cache for token balances
     * @param ethBalanceCache the cache for ETH balances
     * @param priceCache the cache for token prices
     * @param tokenCache the cache for token metadata
     * @param transferCache the cache for transfer history
     */
    public CachedAlchemyApiService(
            BalancesAPI balancesAPI,
            PricesAPI pricesAPI,
            TokenAPI tokenAPI,
            TransfersAPI transfersAPI,
            CacheLayer<String, BalanceResponse> balancesApiCache,
            CacheLayer<String, EthBalanceResponse> ethBalanceCache,
            CacheLayer<String, TokenPriceResponse> priceCache,
            CacheLayer<String, TokenMetadataResponse> tokenCache,
            CacheLayer<String, TransferResponse> transferCache
    ) {
        this.balancesAPI = balancesAPI;
        this.pricesAPI = pricesAPI;
        this.tokenAPI = tokenAPI;
        this.transfersAPI = transfersAPI;
        this.balancesApiCache = balancesApiCache;
        this.ethBalanceCache = ethBalanceCache;
        this.priceCache = priceCache;
        this.tokenCache = tokenCache;
        this.transferCache = transferCache;
    }

    @Override
    public BalanceResponse getTokenBalances(String walletAddress) throws IOException {
        return balancesApiCache.getOrCompute(walletAddress, addr -> {
            try {
                return balancesAPI.getTokenBalances(walletAddress);
            } catch (IOException e) {
                logger.severe("❌ Error fetching token balances for " + addr + ": " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public EthBalanceResponse getEthBalance(String walletAddress) throws IOException {
        return ethBalanceCache.getOrCompute(walletAddress, addr -> {
            try {
                return balancesAPI.getEthBalance(walletAddress);
            } catch (IOException e) {
                logger.severe("❌ Error fetching ETH balance for " + addr + ": " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public TokenPriceResponse getUsdPrice(String tokenAddress) throws Exception {
        return priceCache.getOrCompute("price:" + tokenAddress, addr -> {
            try {
                return pricesAPI.getUsdPrice(tokenAddress);
            } catch (Exception e) {
                logger.severe("❌ Error fetching USD price for " + tokenAddress + ": " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public TokenPriceResponse getUsdPriceBySymbol(String symbol) throws Exception {
        return priceCache.getOrCompute("price:symbol:" + symbol, sym -> {
            try {
                return pricesAPI.getUsdPriceBySymbol(symbol);
            } catch (Exception e) {
                logger.severe("❌ Error fetching USD price by symbol for " + symbol + ": " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public TokenMetadataResponse getTokenMetadata(String tokenAddress) throws Exception {
        return tokenCache.getOrCompute(tokenAddress, addr -> {
            try {
                return tokenAPI.getTokenMetadata(tokenAddress);
            } catch (Exception e) {
                logger.severe("❌ Error fetching token metadata for " + tokenAddress + ": " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public TransferResponse getTransferResponse(String address, int maxCount) throws IOException {
        String cacheKey = address + "::" + maxCount;
        return transferCache.getOrCompute(cacheKey, key -> {
            try {
                return transfersAPI.getTransferResponse(address, maxCount);
            } catch (IOException e) {
                logger.severe("❌ Error fetching transfer response for " + address + ": " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
}
