package com.rbbozkurt.ethmonitor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Response object for token balance retrieval. This class represents the response structure
 * for fetching Ethereum wallet token balances from the API.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceResponse {

    private String jsonrpc;  // JSON-RPC version
    private int id;  // ID of the request
    private Result result;  // Result containing the token balance details

    /**
     * Inner class that holds the result of the balance response.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {

        private String address;  // Ethereum wallet address
        private List<TokenBalance> tokenBalances;  // List of token balances for the address
    }

    /**
     * Inner class that holds the details of a token balance.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TokenBalance {

        private String contractAddress;  // Contract address of the token
        private String tokenBalance;  // The token balance as a hex string (e.g., "0x0")
        private String error;  // Error message if there was an issue, null if no error
    }
}
