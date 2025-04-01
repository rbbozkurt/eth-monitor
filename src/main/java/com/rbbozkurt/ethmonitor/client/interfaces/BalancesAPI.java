package com.rbbozkurt.ethmonitor.client.interfaces;

import com.rbbozkurt.ethmonitor.dto.BalanceResponse;
import com.rbbozkurt.ethmonitor.dto.EthBalanceResponse;

import java.io.IOException;

/**
 * Interface for interacting with the Balances API, which provides methods for retrieving
 * token and ETH balances for a given wallet address.
 */
public interface BalancesAPI {

    /**
     * Retrieves the token balances for a given wallet address.
     *
     * @param walletAddress the Ethereum wallet address to query for token balances
     * @return the {@link BalanceResponse} containing token balances for the specified address
     * @throws IOException if the request fails or the response cannot be parsed
     */
    BalanceResponse getTokenBalances(String walletAddress) throws IOException;

    /**
     * Retrieves the ETH balance for a given wallet address.
     *
     * @param walletAddress the Ethereum wallet address to query for ETH balance
     * @return the {@link EthBalanceResponse} containing the ETH balance for the specified address
     * @throws IOException if the request fails or the response cannot be parsed
     */
    EthBalanceResponse getEthBalance(String walletAddress) throws IOException;
}
