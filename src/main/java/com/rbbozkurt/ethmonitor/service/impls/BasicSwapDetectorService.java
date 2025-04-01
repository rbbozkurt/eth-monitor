package com.rbbozkurt.ethmonitor.service.impls;

import com.rbbozkurt.ethmonitor.model.WalletAnalysisReport.HistoricalTransfer;
import com.rbbozkurt.ethmonitor.service.interfaces.SwapDetectorService;

import java.util.Set;
import java.util.logging.Logger;

/**
 * Basic implementation of the {@link SwapDetectorService} that detects token swaps
 * based on known decentralized exchange (DEX) contract addresses.
 */
public class BasicSwapDetectorService implements SwapDetectorService {

    private static final Logger logger = Logger.getLogger(BasicSwapDetectorService.class.getName());

    // Known DEX router or factory contracts
    private static final Set<String> KNOWN_DEX_CONTRACTS = Set.of(
            "0x5c69bee701ef814a2b6a3edd4b1652cb9cc5aa6f", // UniswapV2Factory
            "0xd9e1ce17f2641f24ae83637ab66a2cca9c378b9f", // SushiSwap
            "0x1111111254eeb25477b68fb85ed929f73a960582", // 1inch
            "0xe592427a0aece92de3edee1f18e0157c05861564", // UniswapV3 Router
            "0xdef1c0ded9bec7f1a1670819833240f027b25eff", // 0x Exchange Proxy
            "0x68b3465833fb72a70ecdf485e0e4c7bd8665fc45", // Uniswap V3 SwapRouter02
            "0x7a250d5630b4cf539739df2c5dacabf31d1c8ed8"  // Uniswap V2 Router
    );

    /**
     * Checks if the given transfer is a token swap.
     *
     * @param transfer the historical transfer to check
     * @return true if the transfer is a token swap, false otherwise
     */
    @Override
    public boolean isSwap(HistoricalTransfer transfer) {
        if (transfer == null) {
            return false;
        }

        String contractAddress = transfer.getRawContractAddress();
        String fromAddress = transfer.getFrom();
        String toAddress = transfer.getTo();

        // Check if the contract address is a known DEX contract, return false if null
        boolean isSwapByContract = contractAddress != null && KNOWN_DEX_CONTRACTS.contains(contractAddress.toLowerCase());

        // Check if the 'from' or 'to' address matches any known DEX contract, return false if null
        boolean isSwapByFromAddress = fromAddress != null && KNOWN_DEX_CONTRACTS.contains(fromAddress.toLowerCase());
        boolean isSwapByToAddress = toAddress != null && KNOWN_DEX_CONTRACTS.contains(toAddress.toLowerCase());

        return isSwapByContract || isSwapByFromAddress || isSwapByToAddress;

    }
}
