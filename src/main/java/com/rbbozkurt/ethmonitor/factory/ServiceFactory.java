package com.rbbozkurt.ethmonitor.factory;

import com.rbbozkurt.ethmonitor.service.interfaces.ApiService;
import com.rbbozkurt.ethmonitor.service.interfaces.BalanceService;
import com.rbbozkurt.ethmonitor.service.interfaces.SwapDetectorService;
import com.rbbozkurt.ethmonitor.service.interfaces.TransferService;

/**
 * Factory interface for creating instances of various services in the Ethereum monitoring application.
 * This includes services for transfers, balances, swap detection, and general API interaction.
 */
public interface ServiceFactory {

    /**
     * Creates or retrieves a cached instance of the {@link TransferService} for the given API keys.
     *
     * @param balancesApiKey the API key for balances
     * @param pricesApiKey the API key for prices
     * @param tokensApiKey the API key for tokens
     * @param transfersApiKey the API key for transfers
     * @return an instance of {@link TransferService}
     */
    TransferService getTransferService(String balancesApiKey, String pricesApiKey, String tokensApiKey, String transfersApiKey);

    /**
     * Creates or retrieves a cached instance of the {@link BalanceService} for the given API keys.
     *
     * @param balancesApiKey the API key for balances
     * @param pricesApiKey the API key for prices
     * @param tokensApiKey the API key for tokens
     * @param transfersApiKey the API key for transfers
     * @return an instance of {@link BalanceService}
     */
    BalanceService getBalanceService(String balancesApiKey, String pricesApiKey, String tokensApiKey, String transfersApiKey);

    /**
     * Creates or retrieves a cached instance of the {@link SwapDetectorService}.
     *
     * @return an instance of {@link SwapDetectorService}
     */
    SwapDetectorService getSwapDetectorService();

    /**
     * Creates or retrieves a cached instance of the {@link ApiService} for the given API keys.
     *
     * @param balancesApiKey the API key for balances
     * @param pricesApiKey the API key for prices
     * @param tokensApiKey the API key for tokens
     * @param transfersApiKey the API key for transfers
     * @return an instance of {@link ApiService}
     */
    ApiService getApiService(String balancesApiKey, String pricesApiKey, String tokensApiKey, String transfersApiKey);
}
