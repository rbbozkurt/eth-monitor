package com.rbbozkurt.ethmonitor.service.impls;

import com.rbbozkurt.ethmonitor.dto.BalanceResponse;
import com.rbbozkurt.ethmonitor.dto.EthBalanceResponse;
import com.rbbozkurt.ethmonitor.dto.TokenMetadataResponse;
import com.rbbozkurt.ethmonitor.dto.TokenPriceResponse;
import com.rbbozkurt.ethmonitor.model.WalletAnalysisReport.TokenBalance;
import com.rbbozkurt.ethmonitor.service.interfaces.ApiService;
import com.rbbozkurt.ethmonitor.service.interfaces.BalanceService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Service implementation for retrieving and processing token and ETH balances
 * for a given wallet address. The balances are fetched from the Alchemy API,
 * and the values are converted to USD using the current token price.
 */
public class AlchemyBalanceService implements BalanceService {

    private static final Logger logger = Logger.getLogger(AlchemyBalanceService.class.getName());

    private static final String ETH_SYMBOL = "ETH";  // Symbol for Ethereum
    private static final String ETH_CONTRACT = "0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";  // Placeholder address for ETH
    private static final int ETH_DECIMALS = 18;  // Decimals for ETH (standard is 18)

    private final ApiService apiService;  // API service for interacting with external APIs
    private final ExecutorService executor;  // Executor for handling asynchronous tasks

    /**
     * Constructs an {@link AlchemyBalanceService} with the given API service and executor.
     *
     * @param apiService the API service used to interact with the Alchemy API
     * @param executor   the executor for handling asynchronous tasks
     */
    public AlchemyBalanceService(ApiService apiService, ExecutorService executor) {
        this.apiService = apiService;
        this.executor = executor;
    }

    /**
     * Retrieves the token balances and their USD values for a given wallet address.
     * This method fetches token balances, their metadata (e.g., symbol, decimals),
     * and the price in USD, and returns a list of {@link TokenBalance} instances.
     *
     * @param walletAddress the Ethereum wallet address for which token balances are fetched
     * @return a list of {@link TokenBalance} representing token balances and their USD values
     */
    @Override
    public List<TokenBalance> getTokenBalancesWithUsd(String walletAddress) {
        List<TokenBalance> result = new ArrayList<>();

        try {
            // Fetch token balances from the API
            BalanceResponse balanceResponse = apiService.getTokenBalances(walletAddress);
            List<BalanceResponse.TokenBalance> balances = balanceResponse.getResult().getTokenBalances();

            List<Callable<TokenBalance>> tasks = new ArrayList<>();

            // Process each token balance asynchronously
            for (BalanceResponse.TokenBalance balance : balances) {
                if (balance.getError() != null || balance.getTokenBalance() == null) continue;

                String tokenAddress = balance.getContractAddress();
                String rawHex = balance.getTokenBalance();
                if (!rawHex.startsWith("0x")) continue;

                BigInteger rawInt = new BigInteger(rawHex.substring(2), 16);
                if (rawInt.equals(BigInteger.ZERO)) continue;

                // Task for processing each token
                Callable<TokenBalance> task = () -> {
                    try {

                        // Fetch token metadata
                        TokenMetadataResponse meta = apiService.getTokenMetadata(tokenAddress);

                        int decimals = meta.getResult().getDecimals();
                        BigDecimal actualBalance = new BigDecimal(rawInt).movePointLeft(decimals);

                        // Fetch token price in USD
                        TokenPriceResponse priceResponse = apiService.getUsdPrice(tokenAddress);

                        BigDecimal priceUsd = priceResponse.getData().stream()
                                .flatMap(d -> d.getPrices().stream())
                                .filter(p -> "usd".equalsIgnoreCase(p.getCurrency()))
                                .map(p -> new BigDecimal(p.getValue()))
                                .findFirst()
                                .orElse(BigDecimal.ZERO);

                        BigDecimal usdValue = priceUsd.multiply(actualBalance).setScale(6, RoundingMode.HALF_UP);

                        // Create and return a TokenBalance object
                        TokenBalance tokenBalance = new TokenBalance();
                        tokenBalance.setTokenSymbol(meta.getResult().getSymbol());
                        tokenBalance.setContractAddress(tokenAddress);
                        tokenBalance.setBalance(actualBalance);
                        tokenBalance.setUsdValue(usdValue);

                        return tokenBalance;

                    } catch (Exception e) {
                        return null;
                    }
                };

                // Add task to the list for asynchronous execution
                tasks.add(task);
            }

            // Execute all tasks asynchronously and collect results
            List<Future<TokenBalance>> futures = executor.invokeAll(tasks);
            for (Future<TokenBalance> future : futures) {
                try {
                    TokenBalance tb = future.get(15, TimeUnit.SECONDS);
                    if (tb != null) {
                        result.add(tb);
                    }
                } catch (TimeoutException e) {
                    logger.warning("⚠️ Token task timed out.");
                }
            }

            // Fetch ETH balance asynchronously
            result.add(getEthBalanceWithUsd(walletAddress));

            return result;

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to fetch or process token balances: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves the Ethereum balance and its USD value for a given wallet address.
     * This method fetches the ETH balance, converts it into the appropriate format,
     * and returns it as a {@link TokenBalance} object.
     *
     * @param walletAddress the Ethereum wallet address for which the ETH balance is fetched
     * @return a {@link TokenBalance} representing the ETH balance and its USD value
     */
    @Override
    public TokenBalance getEthBalanceWithUsd(String walletAddress) {
        try {

            // Fetch ETH balance from the API
            EthBalanceResponse ethResponse = apiService.getEthBalance(walletAddress);
            BigInteger rawBalance = new BigInteger(ethResponse.getResult().substring(2), 16);
            BigDecimal actualBalance = new BigDecimal(rawBalance).movePointLeft(ETH_DECIMALS);

            // Fetch ETH price in USD
            TokenPriceResponse priceResponse = apiService.getUsdPriceBySymbol(ETH_SYMBOL);
            BigDecimal priceUsd = priceResponse.getData().stream()
                    .flatMap(d -> d.getPrices().stream())
                    .filter(p -> "usd".equalsIgnoreCase(p.getCurrency()))
                    .map(p -> new BigDecimal(p.getValue()))
                    .findFirst()
                    .orElse(BigDecimal.ZERO);

            BigDecimal usdValue = priceUsd.multiply(actualBalance).setScale(6, RoundingMode.HALF_UP);

            // Create and return a TokenBalance object for ETH
            TokenBalance ethBalance = new TokenBalance();
            ethBalance.setTokenSymbol(ETH_SYMBOL);
            ethBalance.setContractAddress(ETH_CONTRACT);
            ethBalance.setBalance(actualBalance);
            ethBalance.setUsdValue(usdValue);

            return ethBalance;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch ETH balance", e);
        }
    }
}
