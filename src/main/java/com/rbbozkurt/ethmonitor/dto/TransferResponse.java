package com.rbbozkurt.ethmonitor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Response object for retrieving Ethereum asset transfers from the Alchemy API.
 * This class represents the structure of the transfer response, which contains a list of transfers,
 * associated metadata, and pagination details.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferResponse {

    private String jsonrpc;  // JSON-RPC version
    private int id;  // ID of the request
    private Result result;  // Result containing the list of transfers and pagination information

    /**
     * Inner class that holds the result of the transfer response.
     * Contains a list of transfers and pagination key.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {

        private List<Transfer> transfers;  // List of transfers
        private String pageKey;  // Pagination key for fetching the next page of transfers
    }

    /**
     * Inner class that represents an individual asset transfer.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Transfer {

        private String asset;  // The asset being transferred (e.g., "ETH", "USDC")
        private String from;  // The sender address
        private String to;  // The recipient address
        private String category;  // The transfer category (e.g., "external", "erc20")
        private String blockNum;  // The block number in which the transfer occurred
        private String hash;  // The transaction hash
        private String uniqueId;  // A unique ID for the transfer
        private String value;  // The value of the transfer (usually in the asset's smallest unit)
        private String tokenId;  // Token ID (for token-based transfers like ERC721)
        private RawContract rawContract;  // Raw contract details, if available
        private Metadata metadata;  // Additional metadata associated with the transfer
    }

    /**
     * Inner class representing the raw contract details related to the transfer.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RawContract {

        private String value;  // The value associated with the contract
        private String address;  // The contract address
        private String decimal;  // The number of decimals for the token or asset
    }

    /**
     * Inner class representing additional metadata associated with a transfer.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Metadata {

        private String blockTimestamp;  // Timestamp when the block was mined
    }
}
