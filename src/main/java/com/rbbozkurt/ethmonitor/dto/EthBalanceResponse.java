package com.rbbozkurt.ethmonitor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Response object for retrieving the Ethereum balance of a wallet address.
 * The balance is returned as a hexadecimal string.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EthBalanceResponse {

    private String jsonrpc;  // JSON-RPC version
    private int id;  // ID of the request
    private String result;  // Ethereum balance in hexadecimal format (e.g., "0x5af3107a4000")
}
