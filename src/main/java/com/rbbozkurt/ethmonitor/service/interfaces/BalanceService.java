package com.rbbozkurt.ethmonitor.service.interfaces;

import com.rbbozkurt.ethmonitor.model.WalletAnalysisReport.TokenBalance;

import java.util.List;

/**
 * Service interface for retrieving wallet balances, including both token balances and ETH balance,
 * along with their USD values. This service provides methods for fetching token balances and ETH balance,
 * considering their USD values for reporting purposes.
 */
public interface BalanceService {

    /**
     * Retrieves the token balances with their respective USD values for a given wallet address.
     *
     * @param walletAddress the Ethereum wallet address
     * @return a list of {@link TokenBalance} representing the token balances along with their USD values
     */
    List<TokenBalance> getTokenBalancesWithUsd(String walletAddress);

    /**
     * Retrieves the ETH balance with its USD value for a given wallet address.
     *
     * @param walletAddress the Ethereum wallet address
     * @return a {@link TokenBalance} representing the ETH balance along with its USD value
     */
    TokenBalance getEthBalanceWithUsd(String walletAddress);
}
