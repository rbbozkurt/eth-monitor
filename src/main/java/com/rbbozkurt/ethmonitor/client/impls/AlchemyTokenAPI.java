package com.rbbozkurt.ethmonitor.client.impls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbbozkurt.ethmonitor.client.interfaces.HttpClient;
import com.rbbozkurt.ethmonitor.factory.HttpClientFactory;
import com.rbbozkurt.ethmonitor.client.interfaces.TokenAPI;
import com.rbbozkurt.ethmonitor.dto.TokenMetadataResponse;
import com.rbbozkurt.ethmonitor.cache.CacheLayer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of {@link TokenAPI} that fetches metadata of a token from the Alchemy API
 * and caches the results for future lookups.
 */
public class AlchemyTokenAPI implements TokenAPI {

    private static final Logger logger = Logger.getLogger(AlchemyTokenAPI.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient;

    /**
     * Constructs an {@code AlchemyTokenAPI} with the specified API key, HTTP client factory,
     *
     * @param apiKey the Alchemy API key
     * @param factory the factory for creating an {@link HttpClient}
     */
    public AlchemyTokenAPI(String apiKey, HttpClientFactory factory) {
        this.httpClient = factory.getClient("https://eth-mainnet.g.alchemy.com/v2/" + apiKey);
    }

    /**
     * Retrieves the metadata for a token given its Ethereum address.
     * The result is cached to improve performance on subsequent requests.
     *
     * @param tokenAddress the Ethereum address of the token
     * @return the token metadata
     * @throws Exception if the request or deserialization fails
     */
    @Override
    public TokenMetadataResponse getTokenMetadata(String tokenAddress) throws Exception {
            String requestJson = """
            {
              "jsonrpc": "2.0",
              "id": 1,
              "method": "alchemy_getTokenMetadata",
              "params": ["%s"]
            }
            """.formatted(tokenAddress);
            try {
                TokenMetadataResponse response = httpClient.post(requestJson, TokenMetadataResponse.class);
                logger.fine("✅ Token metadata response received for address " + tokenAddress + ": " +
                        objectMapper.writeValueAsString(response));
                return response;
            } catch (Exception e) {
                logger.log(Level.SEVERE, "❌ Failed to fetch token metadata for address: " + tokenAddress + " | Error: " + e.getMessage(), e);
                throw new RuntimeException("Failed to fetch token metadata", e);
            }

    }
}
