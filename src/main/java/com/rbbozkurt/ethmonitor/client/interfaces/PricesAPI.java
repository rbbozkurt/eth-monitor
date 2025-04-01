package com.rbbozkurt.ethmonitor.client.interfaces;

import com.rbbozkurt.ethmonitor.dto.TokenPriceResponse;

/**
 * Interface for interacting with the Prices API, which provides methods to fetch the USD price
 * of a token either by its Ethereum address or by its symbol.
 */
public interface PricesAPI {

    /**
     * Retrieves the USD price of a token using its Ethereum address.
     *
     * @param tokenAddress the Ethereum address of the token
     * @return a {@link TokenPriceResponse} containing the USD price for the token
     * @throws Exception if the request fails or the response cannot be parsed
     */
    TokenPriceResponse getUsdPrice(String tokenAddress) throws Exception;

    /**
     * Retrieves the USD price of a token using its symbol.
     *
     * @param symbol the symbol of the token (e.g., "ETH", "USDC")
     * @return a {@link TokenPriceResponse} containing the USD price for the token
     * @throws Exception if the request fails or the response cannot be parsed
     */
    TokenPriceResponse getUsdPriceBySymbol(String symbol) throws Exception;
}
