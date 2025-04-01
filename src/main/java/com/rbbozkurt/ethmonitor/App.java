package com.rbbozkurt.ethmonitor;

import com.rbbozkurt.ethmonitor.cli.EthMonitorCLI;
import picocli.CommandLine;

/**
 * Main entry point for the Ethereum Monitoring CLI Application.
 * This class runs the CLI tool by executing the EthMonitorCLI command.
 */
public class App
{
    public static void main(String[] args) {
        // Execute the EthMonitorCLI class with the provided arguments
        int exitCode = new CommandLine(new EthMonitorCLI()).execute(args);

        // Exit the application with the same code as the CLI command
        System.exit(exitCode);
    }
}
