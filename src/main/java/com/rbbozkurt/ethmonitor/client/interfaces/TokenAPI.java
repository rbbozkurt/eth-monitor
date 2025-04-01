package com.rbbozkurt.ethmonitor.client.interfaces;

import com.rbbozkurt.ethmonitor.dto.TokenMetadataResponse;

/**
 * Interface for interacting with the Token API, which provides a method to fetch metadata
 * for a token using its Ethereum address.
 */
public interface TokenAPI {

    /**
     * Retrieves the metadata for a token using its Ethereum address.
     *
     * @param tokenAddress the Ethereum address of the token
     * @return a {@link TokenMetadataResponse} containing the token's metadata
     * @throws Exception if the request fails or the response cannot be parsed
     */
    TokenMetadataResponse getTokenMetadata(String tokenAddress) throws Exception;
}
