package com.rbbozkurt.ethmonitor.service.interfaces;

import com.rbbozkurt.ethmonitor.model.WalletAnalysisReport;

import java.nio.file.Path;

public interface WalletAnalyzer {

    /**
     * Analyzes the given Ethereum wallet and returns a structured report.
     *
     * @param walletAddress Ethereum wallet address
     * @return WalletAnalysisReport containing balance, volume, and stats
     * @throws Exception on processing error
     */
    WalletAnalysisReport analyze(String walletAddress, int maxCount) throws Exception;


        /**
         * Analyzes the given wallet and writes the results to a file.
         *
         * @param walletAddress Ethereum wallet address
         * @param outputFile file path to export the result as JSON or text
         * @throws Exception on processing or I/O error
         */
    void analyzeAndExport(String walletAddress, int maxCount, Path outputFile) throws Exception;
}
