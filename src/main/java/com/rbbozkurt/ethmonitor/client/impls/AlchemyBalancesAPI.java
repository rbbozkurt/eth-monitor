package com.rbbozkurt.ethmonitor.client.impls;

import com.rbbozkurt.ethmonitor.client.interfaces.BalancesAPI;
import com.rbbozkurt.ethmonitor.client.interfaces.HttpClient;
import com.rbbozkurt.ethmonitor.factory.HttpClientFactory;
import com.rbbozkurt.ethmonitor.dto.BalanceResponse;
import com.rbbozkurt.ethmonitor.dto.EthBalanceResponse;

import java.io.IOException;

/**
 * Implementation of the {@link BalancesAPI} that interacts with the Alchemy API
 * to retrieve Ethereum and ERC-20 token balances for a given wallet address.
 * <p>
 */
public class AlchemyBalancesAPI implements BalancesAPI {

    private final HttpClient httpClient;

    /**
     * Constructs a new AlchemyBalancesAPI instance with a specified API key, HTTP client factory
     * @param apiKey              the Alchemy API key
     * @param factory             the factory for creating an {@link HttpClient}
     */
    public AlchemyBalancesAPI(
            String apiKey,
            HttpClientFactory factory
    ) {
        this.httpClient = factory.getClient("https://eth-mainnet.g.alchemy.com/v2/" + apiKey);
    }

    /**
     * Retrieves the token balances for the given wallet address using the Alchemy API.
     *
     * @param walletAddress the wallet address to query
     * @return the token balances for the wallet
     * @throws IOException if the HTTP request fails
     */
    @Override
    public BalanceResponse getTokenBalances(String walletAddress) throws IOException {
            String requestBody = """
                {
                  "jsonrpc": "2.0",
                  "id": 1,
                  "method": "alchemy_getTokenBalances",
                  "params": ["%s"]
                }
                """.formatted(walletAddress);
            try {
                return httpClient.post(requestBody, BalanceResponse.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to fetch token balances", e);
            }

    }

    /**
     * Retrieves the ETH balance for the given wallet address using the Alchemy API.
     *
     * @param walletAddress the wallet address to query
     * @return the ETH balance for the wallet
     * @throws IOException if the HTTP request fails
     */
    @Override
    public EthBalanceResponse getEthBalance(String walletAddress) throws IOException {
            String requestBody = """
                {
                  "jsonrpc": "2.0",
                  "id": 1,
                  "method": "eth_getBalance",
                  "params": ["%s", "latest"]
                }
                """.formatted(walletAddress);
            try {
                return httpClient.post(requestBody, EthBalanceResponse.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to fetch ETH balance", e);
            }
    }
}
