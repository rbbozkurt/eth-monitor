package com.rbbozkurt.ethmonitor.service.impls;

import com.rbbozkurt.ethmonitor.dto.TransferResponse;
import com.rbbozkurt.ethmonitor.model.WalletAnalysisReport.HistoricalTransfer;
import com.rbbozkurt.ethmonitor.service.interfaces.ApiService;
import com.rbbozkurt.ethmonitor.service.interfaces.TransferService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Service implementation for retrieving and processing historical transfers for a given wallet address.
 * This service fetches the transfer data from the API, processes it asynchronously,
 * and converts the transfer data into {@link HistoricalTransfer} objects.
 */
public class AlchemyTransferService implements TransferService {

    private static final Logger logger = Logger.getLogger(AlchemyTransferService.class.getName());

    private final ApiService apiService;
    private final ExecutorService virtualThreadExecutor;

    /**
     * Constructs an {@link AlchemyTransferService} with the given API service and executor.
     *
     * @param apiService the API service used to interact with the Alchemy API
     * @param virtualThreadExecutor the executor for handling asynchronous tasks
     */
    public AlchemyTransferService(
            ApiService apiService,
            ExecutorService virtualThreadExecutor
    ) {
        this.apiService = apiService;
        this.virtualThreadExecutor = virtualThreadExecutor;
    }

    /**
     * Retrieves historical transfer details for a given wallet address, including transaction data
     * and associated metadata. The transfers are fetched from the Alchemy API and processed asynchronously.
     *
     * @param walletAddress the Ethereum wallet address for which historical transfers are fetched
     * @param maxCount the maximum number of transfers to retrieve
     * @return a list of {@link HistoricalTransfer} objects representing the transfers
     */
    @Override
    public List<HistoricalTransfer> getHistoricalTransfers(String walletAddress, int maxCount) {
        try {
            // Fetch transfer response from API
            TransferResponse response = apiService.getTransferResponse(walletAddress, maxCount);
            List<TransferResponse.Transfer> transfers = response.getResult().getTransfers();

            // Convert each transfer into a HistoricalTransfer object asynchronously
            List<Callable<HistoricalTransfer>> tasks = transfers.stream()
                    .map(tx -> (Callable<HistoricalTransfer>) () -> {
                        HistoricalTransfer hist = new HistoricalTransfer();
                        hist.setTxHash(tx.getHash());
                        hist.setFrom(tx.getFrom());
                        hist.setTo(tx.getTo());
                        hist.setAsset(tx.getAsset());
                        hist.setCategory(tx.getCategory());
                        hist.setValue(new BigDecimal(tx.getValue() != null ? tx.getValue() : "0"));
                        hist.setTimestamp(Instant.parse(tx.getMetadata().getBlockTimestamp()));
                        hist.setRawContractAddress(tx.getRawContract() != null ? tx.getRawContract().getAddress() : null);
                        return hist;
                    })
                    .collect(Collectors.toList());

            // Execute the tasks and collect results
            List<Future<HistoricalTransfer>> futures = virtualThreadExecutor.invokeAll(tasks);
            List<HistoricalTransfer> result = new CopyOnWriteArrayList<>();

            // Process results
            for (Future<HistoricalTransfer> future : futures) {
                try {
                    HistoricalTransfer histTransfer = future.get();
                    result.add(histTransfer);
                } catch (InterruptedException | ExecutionException e) {
                    logger.severe("❌ Error processing transfer: " + e.getMessage());
                }
            }

            return result;

        } catch (Exception e) {
             throw new RuntimeException("Failed to fetch or process transfers", e);
        }
    }
}
