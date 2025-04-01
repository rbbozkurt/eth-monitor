package com.rbbozkurt.ethmonitor.service.interfaces;

import com.rbbozkurt.ethmonitor.model.WalletAnalysisReport.HistoricalTransfer;

/**
 * Service interface for detecting whether a given transfer represents a token swap.
 * This service is used to identify token swap transactions, typically involving
 * decentralized exchanges (DEXs).
 */
public interface SwapDetectorService {

    /**
     * Determines if the given transfer is a token swap.
     *
     * @param transfer the historical transfer to check
     * @return true if the transfer is identified as a swap, false otherwise
     */
    boolean isSwap(HistoricalTransfer transfer);
}
