package com.rbbozkurt.ethmonitor.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * A detailed model representing a report for analyzing a wallet's transactions, balances, and activity.
 * This report includes details like the wallet address, total transaction count, token balances, transfer history,
 * and estimated swap counts.
 */
@Data
public class WalletAnalysisReport {

    private String walletAddress;  // The Ethereum wallet address being analyzed
    private int totalTransactionCount;  // Total number of transactions associated with the wallet
    private BigDecimal totalVolumeUsd;  // Total transaction volume in USD for the wallet
    private int estimatedSwapCount;  // Estimated count of swaps (like token-to-token swaps)
    private List<TokenBalance> balances;  // List of token balances for the wallet
    private List<HistoricalTransfer> transfers;  // List of historical transfers for the wallet
    private BigDecimal totalBalanceUsd;  // The total balance in USD, considering all tokens

    /**
     * Represents the balance details for a specific token in the wallet.
     */
    @Data
    public static class TokenBalance {

        private String tokenSymbol;  // The symbol of the token (e.g., "ETH", "USDC")
        private String contractAddress;  // The contract address of the token on the Ethereum network
        private BigDecimal balance;  // The balance of the token in the wallet
        private BigDecimal usdValue;  // The equivalent value of the token balance in USD
    }

    /**
     * Represents the details of a historical transfer made by the wallet.
     */
    @Data
    public static class HistoricalTransfer {

        private String txHash;  // The transaction hash associated with the transfer
        private Instant timestamp;  // The timestamp when the transfer occurred
        private String from;  // The address from which the asset was sent
        private String to;  // The address to which the asset was sent
        private String asset;  // The asset type (e.g., "ETH", "USDC")
        private BigDecimal value;  // The value of the transfer
        private String category;  // The category of the transfer (e.g., "external", "erc20", etc.)
        private String rawContractAddress;  // The raw contract address involved in the transfer (if applicable)
    }
}
