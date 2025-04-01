package com.rbbozkurt.ethmonitor.service.impls;

import com.rbbozkurt.ethmonitor.service.interfaces.BalanceService;
import com.rbbozkurt.ethmonitor.service.interfaces.SwapDetectorService;
import com.rbbozkurt.ethmonitor.service.interfaces.TransferService;
import com.rbbozkurt.ethmonitor.service.interfaces.WalletAnalyzer;

import java.util.logging.Logger;

/**
 * A builder class for constructing instances of {@link WalletAnalyzer} with the required services.
 * The builder ensures that all necessary dependencies are provided before creating the final {@link AlchemyWalletAnalyzer}.
 */
public class AlchemyWalletAnalyzerBuilder {

    private static final Logger logger = Logger.getLogger(AlchemyWalletAnalyzerBuilder.class.getName());

    private TransferService transferService;
    private BalanceService balanceService;
    private SwapDetectorService swapDetectorService;

    /**
     * Sets the {@link TransferService} for the builder.
     *
     * @param transferService the transfer service
     * @return the builder instance for method chaining
     */
    public AlchemyWalletAnalyzerBuilder withTransferService(TransferService transferService) {
        this.transferService = transferService;
        return this;
    }

    /**
     * Sets the {@link BalanceService} for the builder.
     *
     * @param balanceService the balance service
     * @return the builder instance for method chaining
     */
    public AlchemyWalletAnalyzerBuilder withBalanceService(BalanceService balanceService) {
        this.balanceService = balanceService;
        return this;
    }

    /**
     * Sets the {@link SwapDetectorService} for the builder.
     *
     * @param swapDetectorService the swap detector service
     * @return the builder instance for method chaining
     */
    public AlchemyWalletAnalyzerBuilder withSwapDetectorService(SwapDetectorService swapDetectorService) {
        this.swapDetectorService = swapDetectorService;
        return this;
    }

    /**
     * Builds and returns an instance of {@link WalletAnalyzer} using the provided services.
     * Throws an exception if any of the required services are missing.
     *
     * @return an instance of {@link WalletAnalyzer}
     * @throws IllegalStateException if any required services are not set
     */
    public WalletAnalyzer build() {
        // Ensure all dependencies are provided
        if (transferService == null || balanceService == null || swapDetectorService == null) {
            throw new IllegalStateException("All dependencies must be set");
        }

        // Return a new instance of AlchemyWalletAnalyzer
        return new AlchemyWalletAnalyzer(transferService, balanceService, swapDetectorService);
    }
}
