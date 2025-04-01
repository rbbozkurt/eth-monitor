package com.rbbozkurt.ethmonitor.factory;

import com.rbbozkurt.ethmonitor.client.interfaces.BalancesAPI;
import com.rbbozkurt.ethmonitor.client.interfaces.PricesAPI;
import com.rbbozkurt.ethmonitor.client.interfaces.TokenAPI;
import com.rbbozkurt.ethmonitor.client.interfaces.TransfersAPI;

/**
 * Interface for creating instances of various API clients for interacting with the Alchemy API.
 * This factory provides methods to create instances of {@link PricesAPI}, {@link TokenAPI},
 * {@link TransfersAPI}, and {@link BalancesAPI}, each requiring an API key.
 */
public interface APIFactory {

    /**
     * Creates an instance of the {@link PricesAPI} for the given API key.
     *
     * @param apiKey the Alchemy API key
     * @return an instance of {@link PricesAPI}
     */
    PricesAPI createPricesAPI(String apiKey);

    /**
     * Creates an instance of the {@link TokenAPI} for the given API key.
     *
     * @param apiKey the API key
     * @return an instance of {@link TokenAPI}
     */
    TokenAPI createTokenAPI(String apiKey);

    /**
     * Creates an instance of the {@link TransfersAPI} for the given API key.
     *
     * @param apiKey the API key
     * @return an instance of {@link TransfersAPI}
     */
    TransfersAPI createTransfersAPI(String apiKey);

    /**
     * Creates an instance of the {@link BalancesAPI} for the given API key.
     *
     * @param apiKey the API key
     * @return an instance of {@link BalancesAPI}
     */
    BalancesAPI createBalancesAPI(String apiKey);
}
