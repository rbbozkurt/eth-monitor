package com.rbbozkurt.ethmonitor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Response object for retrieving token prices from the Alchemy API.
 * This class represents the structure of the response for fetching token prices in various currencies.
 * Example response:
 * <pre>
 * {
 *   "data": [
 *     {
 *       "network": "eth-mainnet",
 *       "address": "0xa0b86991c6218b36c1d19d4a2e9eb0ce3606eb48",
 *       "prices": [
 *         {
 *           "currency": "usd",
 *           "value": "1.000020280638",
 *           "lastUpdatedAt": "2025-03-31T16:53:12.991754200Z"
 *         }
 *       ]
 *     }
 *   ]
 * }
 * </pre>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenPriceResponse {

    private List<TokenPriceEntry> data;  // List of token price entries

    /**
     * Inner class that represents a token price entry containing network, token address, and associated prices.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TokenPriceEntry {

        private String network;  // Network of the token (e.g., "eth-mainnet")
        private String address;  // Ethereum address of the token
        private List<TokenPrice> prices;  // List of token prices in different currencies
        private TokenError error;  // Error object if an error occurred during the price retrieval
    }

    /**
     * Inner class representing an error message related to token price retrieval.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TokenError {

        private String message;  // Error message if any
    }

    /**
     * Inner class representing the price information of a token.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TokenPrice {

        private String currency;  // Currency of the price (e.g., "usd")
        private String value;  // The value of the token in the specified currency
        private String lastUpdatedAt;  // Timestamp when the price was last updated
    }
}
