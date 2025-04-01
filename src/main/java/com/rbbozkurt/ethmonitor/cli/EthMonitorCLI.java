package com.rbbozkurt.ethmonitor.cli;

import com.rbbozkurt.ethmonitor.service.impls.AlchemyWalletAnalyzerBuilder;
import com.rbbozkurt.ethmonitor.service.interfaces.SwapDetectorService;
import com.rbbozkurt.ethmonitor.service.interfaces.WalletAnalyzer;
import com.rbbozkurt.ethmonitor.model.WalletAnalysisReport;
import com.rbbozkurt.ethmonitor.factory.AlchemyServiceFactory;
import com.rbbozkurt.ethmonitor.service.interfaces.BalanceService;
import com.rbbozkurt.ethmonitor.service.interfaces.TransferService;
import com.rbbozkurt.ethmonitor.util.AddressUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

/**
 * Command-line tool for monitoring an Ethereum address, analyzing transaction stats, token balances, and more.
 * Supports different API keys for different services (Balances, Prices, Tokens, and Transfers).
 */
@Command(
        name = "eth-monitor",
        mixinStandardHelpOptions = true,
        version = "2.0",
        description = "Monitors an Ethereum address and prints transaction & token stats."
)
public class EthMonitorCLI implements Callable<Integer> {

    @Option(
            names = {"-a", "--address"},
            description = "Ethereum address to monitor"
    )
    private String address;

    @Option(
            names = {"-t", "--transfers"},
            description = "Number of recent transfers to fetch (default: 1000)",
            defaultValue = "1000"
    )
    private int transferCount;

    @Option(
            names = {"-b", "--balances-api-key"},
            required = true,
            description = "API key for BalancesAPI"
    )
    private String balancesApiKey;

    @Option(
            names = {"-p", "--prices-api-key"},
            required = true,
            description = "API key for PricesAPI"
    )
    private String pricesApiKey;

    @Option(
            names = {"-k", "--tokens-api-key"},
            required = true,
            description = "API key for TokenAPI"
    )
    private String tokensApiKey;

    @Option(
            names = {"-f", "--transfers-api-key"},
            required = true,
            description = "API key for TransfersAPI"
    )
    private String transfersApiKey;

    @Override
    public Integer call() {

        // Fetch services using different API keys for each service
        TransferService transferService = AlchemyServiceFactory.getInstance()
                .getTransferService(balancesApiKey, pricesApiKey, tokensApiKey, transfersApiKey);

        BalanceService balanceService = AlchemyServiceFactory.getInstance()
                .getBalanceService(balancesApiKey, pricesApiKey, tokensApiKey, transfersApiKey);

        SwapDetectorService swapDetectorService = AlchemyServiceFactory.getInstance()
                .getSwapDetectorService();

        // Build the wallet analyzer
        WalletAnalyzer analyzer = new AlchemyWalletAnalyzerBuilder()
                .withTransferService(transferService)
                .withBalanceService(balanceService)
                .withSwapDetectorService(swapDetectorService)
                .build();

        // Validate the address
        if (address == null || address.isEmpty()) {
            System.err.println("❌ Address is required.");
            return 1;
        }

        if (!AddressUtils.isValidAddress(address)) {
            System.err.println("❌ Invalid Ethereum address.");
            return 1;
        }

        // Print the stats for the given address
        printStats(analyzer, address, transferCount);

        return 0;
    }

    private void printStats(WalletAnalyzer analyzer, String address, int maxCount) {
        try {
            System.out.println("\n🚀 Starting analysis for address: " + address);
            WalletAnalysisReport report = analyzer.analyze(address, maxCount);

            System.out.println("\n📊 Stats for " + report.getWalletAddress());
            System.out.println("────────────────────────────────────");
            System.out.println("🔁 Total Transactions: " + report.getTotalTransactionCount());
            System.out.println("↔️  Estimated Swaps: " + report.getEstimatedSwapCount());
            System.out.printf("💰 Total Volume USD: $%.6f%n", report.getTotalVolumeUsd());
            System.out.printf("💼 Total Balance USD: $%.6f%n", report.getTotalBalanceUsd());

            System.out.println("\n🔍 Token Balances:");
            for (var b : report.getBalances()) {
                System.out.printf("Token: %s (%s)\n", b.getTokenSymbol(), b.getContractAddress());
                System.out.printf("Balance: %s | USD: $%.6f%n", b.getBalance(), b.getUsdValue());
                System.out.println("------------------------------------");
            }

            System.out.println("\n📜 Historical Transfers (latest " + report.getTransfers().size() + "):");
            for (var tx : report.getTransfers()) {
                System.out.printf("[%s] %s %s → %s %s (%s)\n",
                        tx.getTimestamp(), tx.getAsset(), tx.getFrom(), tx.getTo(),
                        tx.getValue(), tx.getCategory());
            }

            System.out.println("✅ Done.");
        } catch (Exception e) {
            System.err.println("❌ Error during analysis: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
