package com.rbbozkurt.ethmonitor.client.impls;

import com.rbbozkurt.ethmonitor.cache.CacheLayer;
import com.rbbozkurt.ethmonitor.client.interfaces.HttpClient;
import com.rbbozkurt.ethmonitor.factory.HttpClientFactory;
import com.rbbozkurt.ethmonitor.client.interfaces.PricesAPI;
import com.rbbozkurt.ethmonitor.dto.TokenPriceResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the {@link PricesAPI} using the Alchemy Prices API to fetch USD prices
 * for tokens by address or symbol, with support for response caching.
 */
public class AlchemyPricesAPI implements PricesAPI {

    private static final Logger logger = Logger.getLogger(AlchemyPricesAPI.class.getName());

    private static final String POST_BASE_URL = "https://api.g.alchemy.com/prices/v1";
    private static final String GET_BASE_URL = "https://api.g.alchemy.com/prices/v1";

    private final String apiKey;
    private final HttpClient postClient;
    private final HttpClient getClient;

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructs an {@code AlchemyPricesAPI} with the given API key, HTTP client factory,
     *
     * @param apiKey              Alchemy API key
     * @param factory             HTTP client factory
     */
    public AlchemyPricesAPI(
            String apiKey,
            HttpClientFactory factory
    ) {
        this.apiKey = apiKey;
        String postUrl = POST_BASE_URL + "/" + apiKey + "/tokens/by-address";
        this.postClient = factory.getClient(postUrl);
        this.getClient = factory.getClient(GET_BASE_URL);
    }

    /**
     * Gets the USD price for a token by its Ethereum address using the Alchemy Prices API.
     *
     * @param tokenAddress Ethereum address of the token
     * @return {@link TokenPriceResponse} containing the price info
     * @throws Exception if the API request or parsing fails
     */
    @Override
    public TokenPriceResponse getUsdPrice(String tokenAddress) throws Exception {
            String requestBody = String.format(
                    "{\"addresses\":[{\"network\":\"eth-mainnet\",\"address\":\"%s\"}]}",
                    tokenAddress
            );
            try {
                TokenPriceResponse response = postClient.post(requestBody, TokenPriceResponse.class);
                logger.fine("✅ Price response received for address " + tokenAddress + ": " +
                        mapper.writeValueAsString(response));
                return response;
            } catch (Exception e) {
                logger.log(Level.SEVERE, "❌ Failed to fetch price by address: " + tokenAddress + " | Error: " + e.getMessage(), e);
                throw new RuntimeException("Failed to fetch price by address", e);
            }
    }

    /**
     * Gets the USD price for a token by its symbol using the Alchemy Prices API.
     *
     * @param symbol token symbol (e.g. ETH, USDC)
     * @return {@link TokenPriceResponse} containing the price info
     * @throws Exception if the API request or parsing fails
     */
    @Override
    public TokenPriceResponse getUsdPriceBySymbol(String symbol) throws Exception {
            String fullUrl = String.format("%s/%s/tokens/by-symbol?symbols=%s", GET_BASE_URL, apiKey, symbol);
            try {
                TokenPriceResponse response = getClient.get(fullUrl, TokenPriceResponse.class);
                logger.fine("✅ Price response received for symbol " + symbol + ": " +
                        mapper.writeValueAsString(response));
                return response;
            } catch (Exception e) {
                logger.log(Level.SEVERE, "❌ Failed to fetch price by symbol: " + symbol + " | Error: " + e.getMessage(), e);
                throw new RuntimeException("Failed to fetch price by symbol", e);
            }
    }
}
