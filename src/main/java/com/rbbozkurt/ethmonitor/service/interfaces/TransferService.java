package com.rbbozkurt.ethmonitor.service.interfaces;

import com.rbbozkurt.ethmonitor.model.WalletAnalysisReport.HistoricalTransfer;

import java.util.List;

/**
 * Service interface for retrieving historical transfer data for a given Ethereum wallet address.
 * This service provides methods for fetching transaction details, including token transfers and
 * other relevant metadata.
 */
public interface TransferService {

    /**
     * Retrieves the historical transfers for a given wallet address, up to a specified maximum count.
     * This method returns a list of transfer details that include information such as the asset transferred,
     * the sender, receiver, and transaction value.
     *
     * @param walletAddress the Ethereum wallet address to fetch historical transfers for
     * @param maxCount the maximum number of transfers to retrieve
     * @return a list of {@link HistoricalTransfer} objects representing the wallet's transfer history
     */
    List<HistoricalTransfer> getHistoricalTransfers(String walletAddress, int maxCount);
}
