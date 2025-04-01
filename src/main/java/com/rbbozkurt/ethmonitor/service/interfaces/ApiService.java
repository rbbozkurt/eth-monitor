package com.rbbozkurt.ethmonitor.service.interfaces;

import com.rbbozkurt.ethmonitor.dto.*;
import com.rbbozkurt.ethmonitor.model.WalletAnalysisReport;

import java.io.IOException;
import java.util.List;

/**
 * Interface for interacting with various APIs to fetch wallet-related data,
 * such as token balances, ETH balance, token price, token metadata, and transfer history.
 * This service provides methods for retrieving on-chain data for an Ethereum wallet.
 */
public interface ApiService {

    /**
     * Retrieves the token balances for a specified wallet address.
     *
     * @param walletAddress the Ethereum wallet address
     * @return a {@link BalanceResponse} containing the token balances
     * @throws IOException if there is an issue fetching the data from the API
     */
    BalanceResponse getTokenBalances(String walletAddress) throws IOException;

    /**
     * Retrieves the ETH balance for a specified wallet address.
     *
     * @param walletAddress the Ethereum wallet address
     * @return an {@link EthBalanceResponse} containing the ETH balance
     * @throws IOException if there is an issue fetching the data from the API
     */
    EthBalanceResponse getEthBalance(String walletAddress) throws IOException;

    /**
     * Retrieves the USD price for a specified token address.
     *
     * @param tokenAddress the Ethereum contract address of the token
     * @return a {@link TokenPriceResponse} containing the USD price of the token
     * @throws Exception if there is an issue fetching the price data
     */
    TokenPriceResponse getUsdPrice(String tokenAddress) throws Exception;

    /**
     * Retrieves the USD price for a token by its symbol (e.g., "ETH", "USDC").
     *
     * @param symbol the symbol of the token
     * @return a {@link TokenPriceResponse} containing the USD price of the token by symbol
     * @throws Exception if there is an issue fetching the price data
     */
    TokenPriceResponse getUsdPriceBySymbol(String symbol) throws Exception;

    /**
     * Retrieves metadata for a specified token address, such as symbol, decimals, etc.
     *
     * @param tokenAddress the Ethereum contract address of the token
     * @return a {@link TokenMetadataResponse} containing the token metadata
     * @throws Exception if there is an issue fetching the metadata
     */
    TokenMetadataResponse getTokenMetadata(String tokenAddress) throws Exception;

    /**
     * Retrieves the transfer history for a specified wallet address, up to a specified count.
     *
     * @param address the Ethereum wallet address
     * @param maxCount the maximum number of transfers to fetch
     * @return a {@link TransferResponse} containing the transfer history for the wallet
     * @throws IOException if there is an issue fetching the transfer data from the API
     */
    TransferResponse getTransferResponse(String address, int maxCount) throws IOException;
}
