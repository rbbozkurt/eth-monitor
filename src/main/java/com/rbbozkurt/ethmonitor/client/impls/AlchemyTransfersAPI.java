package com.rbbozkurt.ethmonitor.client.impls;

import com.rbbozkurt.ethmonitor.factory.HttpClientFactory;
import com.rbbozkurt.ethmonitor.client.interfaces.TransfersAPI;
import com.rbbozkurt.ethmonitor.client.interfaces.HttpClient;
import com.rbbozkurt.ethmonitor.dto.TransferResponse;
import com.rbbozkurt.ethmonitor.dto.TransferResponse.Transfer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of the {@link TransfersAPI} interface that interacts with the Alchemy API
 * to retrieve Ethereum asset transfers for a given address, with caching for improved performance.
 */

public class AlchemyTransfersAPI implements TransfersAPI {

    private static final Logger logger = Logger.getLogger(AlchemyTokenAPI.class.getName());

    private static final int PAGE_SIZE = 100;
    private final HttpClient httpClient;

    /**
     * Constructs a new {@code AlchemyTransfersAPI} with the specified API key, HTTP client factory,
     * and cache layer for storing transfer responses.
     *
     * @param apiKey the Alchemy API key
     * @param factory the factory for creating an {@link HttpClient}
     */
    public AlchemyTransfersAPI(String apiKey, HttpClientFactory factory) {
        this.httpClient = factory.getClient("https://eth-mainnet.g.alchemy.com/v2/" + apiKey);
    }

    /**
     * Retrieves the transfer response for the specified address, with a limit on the number of transfers to fetch.
     *
     * @param address the Ethereum address to query for transfers
     * @param maxCount the maximum number of transfers to fetch
     * @return the transfer response containing a list of transfers
     * @throws IOException if the API request fails or the response cannot be parsed
     */
    @Override
    public TransferResponse getTransferResponse(String address, int maxCount) throws IOException {
        List<Transfer> allTransfers = new ArrayList<>();
        String pageKey = null;
        int remaining = maxCount;

        do {
            String requestBody = buildRequestBody(address, remaining, pageKey);

            TransferResponse response;
                try {
                    response =  httpClient.post(requestBody, TransferResponse.class);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to fetch transfer response", e);
                }


            if (response.getResult() != null && response.getResult().getTransfers() != null) {
                allTransfers.addAll(response.getResult().getTransfers());
                pageKey = response.getResult().getPageKey();
                remaining = maxCount - allTransfers.size();
            } else {
                break;
            }

        } while (pageKey != null && remaining > 0);

        TransferResponse finalResponse = new TransferResponse();
        TransferResponse.Result result = new TransferResponse.Result();
        result.setTransfers(allTransfers);
        finalResponse.setJsonrpc("2.0");
        finalResponse.setId(1);
        finalResponse.setResult(result);
        return finalResponse;
    }

    /**
     * Builds the request body for the Alchemy API request to fetch asset transfers.
     *
     * @param address the Ethereum address to query
     * @param count the number of transfers to request
     * @param pageKey the pagination key for subsequent pages (null if not used)
     * @return the JSON request body as a string
     */
    private String buildRequestBody(String address, int count, String pageKey) {
        String base = """
    {
      "jsonrpc": "2.0",
      "id": 1,
      "method": "alchemy_getAssetTransfers",
      "params": [{
        "fromBlock": "0x0",
        "toBlock": "latest",
        "toAddress": "%s",
        "category": ["external", "erc20", "internal", "erc721", "erc1155", "specialnft"],
        "withMetadata": true,
        "excludeZeroValue": true,
        "maxCount": "0x%s"%s
      }]
    }
    """;

        String pageKeyJson = pageKey != null ? ",\n        \"pageKey\": \"" + pageKey + "\"" : "";
        return base.formatted(address.toLowerCase(), Integer.toHexString(Math.min(PAGE_SIZE, count)), pageKeyJson);
    }

}
