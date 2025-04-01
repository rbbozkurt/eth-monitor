package com.rbbozkurt.ethmonitor.client.interfaces;

import com.rbbozkurt.ethmonitor.dto.TransferResponse;

import java.io.IOException;

/**
 * Interface for interacting with the Transfers API, which provides a method to fetch Ethereum asset transfers
 * for a given wallet address.
 */
public interface TransfersAPI {

    /**
     * Retrieves the transfer response for a given Ethereum address with a limit on the number of transfers to fetch.
     *
     * @param address the Ethereum address to query for asset transfers
     * @param maxCount the maximum number of transfers to retrieve
     * @return a {@link TransferResponse} containing the list of transfers
     * @throws IOException if the request fails or the response cannot be parsed
     */
    TransferResponse getTransferResponse(String address, int maxCount) throws IOException;
}
