package com.rbbozkurt.ethmonitor.dto;

import lombok.Data;

/**
 * Response object for retrieving token metadata information, such as symbol, name, logo, and decimals.
 * This class is used to fetch detailed information about a token from an Ethereum contract.
 *
 * Example full JSON-RPC response:
 * <pre>
 * {
 *   "jsonrpc": "2.0",
 *   "id": 1,
 *   "result": {
 *     "decimals": 6,
 *     "logo": "https://static.alchemyapi.io/images/assets/3408.png",
 *     "name": "USDC",
 *     "symbol": "USDC"
 *   }
 * }
 * </pre>
 */
@Data
public class TokenMetadataResponse {

    private String jsonrpc;  // JSON-RPC version
    private int id;  // ID of the request
    private Result result;  // Result containing the token metadata

    /**
     * Inner class that holds the metadata details of the token.
     */
    @Data
    public static class Result {
        private int decimals;  // Number of decimal places for the token
        private String logo;  // URL to the token logo image
        private String name;  // Name of the token (e.g., "USD Coin")
        private String symbol;  // Symbol of the token (e.g., "USDC")
    }
}
