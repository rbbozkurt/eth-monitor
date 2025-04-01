package com.rbbozkurt.ethmonitor.service.impls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbbozkurt.ethmonitor.model.WalletAnalysisReport;
import com.rbbozkurt.ethmonitor.model.WalletAnalysisReport.HistoricalTransfer;
import com.rbbozkurt.ethmonitor.model.WalletAnalysisReport.TokenBalance;
import com.rbbozkurt.ethmonitor.service.interfaces.BalanceService;
import com.rbbozkurt.ethmonitor.service.interfaces.SwapDetectorService;
import com.rbbozkurt.ethmonitor.service.interfaces.TransferService;
import com.rbbozkurt.ethmonitor.service.interfaces.WalletAnalyzer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service implementation for analyzing wallet data, including historical transfers, token balances, swap counts,
 * and transaction volumes. The results are returned as a detailed report.
 */
public class AlchemyWalletAnalyzer implements WalletAnalyzer {

    private static final Logger logger = Logger.getLogger(AlchemyWalletAnalyzer.class.getName());

    private final TransferService transferService;
    private final BalanceService balanceService;
    private final SwapDetectorService swapDetectorService;

    /**
     * Constructs an {@link AlchemyWalletAnalyzer} with the given services.
     *
     * @param transferService the service used to fetch historical transfers
     * @param balanceService the service used to fetch token balances
     * @param swapDetectorService the service used to detect swaps
     */
    public AlchemyWalletAnalyzer(
            TransferService transferService,
            BalanceService balanceService,
            SwapDetectorService swapDetectorService) {
        this.transferService = transferService;
        this.balanceService = balanceService;
        this.swapDetectorService = swapDetectorService;
    }

    /**
     * Analyzes the wallet's historical transfers, token balances, swap counts, and transaction volume.
     * This method fetches the data and calculates the totals, including USD values.
     *
     * @param address the Ethereum wallet address to analyze
     * @param maxCount the maximum number of historical transfers to fetch
     * @return a {@link WalletAnalysisReport} containing the wallet's analysis details
     * @throws Exception if any error occurs while fetching or processing data
     */
    @Override
    public WalletAnalysisReport analyze(String address, int maxCount) throws Exception {
        List<HistoricalTransfer> transfers = transferService.getHistoricalTransfers(address, maxCount);

        // Fetch token balances with USD values
        List<TokenBalance> balances = balanceService.getTokenBalancesWithUsd(address);

        // Count the number of swap transactions
        int swapCount = (int) transfers.stream()
                .filter(t -> swapDetectorService.isSwap(t))
                .count();

        // Calculate total volume in USD
        BigDecimal totalVolUsd = transfers.stream()
                .map(HistoricalTransfer::getValue)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate total balance in USD
        BigDecimal totalBalanceUsd = balances.stream()
                .map(TokenBalance::getUsdValue)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(6, RoundingMode.HALF_UP);

        // Prepare the report
        WalletAnalysisReport report = new WalletAnalysisReport();
        report.setWalletAddress(address);
        report.setTransfers(transfers);
        report.setBalances(balances);
        report.setEstimatedSwapCount(swapCount);
        report.setTotalTransactionCount(transfers.size());
        report.setTotalVolumeUsd(totalVolUsd.setScale(6, RoundingMode.HALF_UP));
        report.setTotalBalanceUsd(totalBalanceUsd);

        return report;
    }

    /**
     * Analyzes the wallet's data and exports the result as a JSON file.
     * The result is written to the specified output file.
     *
     * @param address the Ethereum wallet address to analyze
     * @param maxCount the maximum number of historical transfers to fetch
     * @param outputFile the output file where the analysis report will be saved
     * @throws Exception if any error occurs while processing data or writing the output file
     */
    @Override
    public void analyzeAndExport(String address, int maxCount, Path outputFile) throws Exception {

        // Get the analysis report
        WalletAnalysisReport report = analyze(address, maxCount);

        // Convert the report to a JSON string
        String json = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(report);

        // Write the JSON string to the output file
        Files.writeString(outputFile, json);
    }
}
