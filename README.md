# Ethereum Blockchain Address Monitor

## Overview
This project is a Java application designed to monitor an Ethereum address, fetch its historical transactions, and compute various statistics such as the number of swaps, overall transaction volume in USD, and the account balance of all assets in USD. The application uses the Alchemy API to interact with the Ethereum blockchain, retrieve data, and perform computations.

## Features

- Monitor an Ethereum address for historical transactions.
- Detect token swaps using known DEX contract addresses.
- Compute the overall transaction volume in USD.
- Calculate and display the account balance for all assets in USD.
- Handle data asynchronously for improved performance and scalability.

## Approach
The project leverages the following key components and design patterns:

1. **Alchemy API:** The application uses the Alchemy API to fetch token balances, ETH balance, and historical transfer data for the provided Ethereum address.
2. **Asynchronous Processing:** To enhance the performance of the application, Java ExecutorService is used to handle token and transfer processing tasks concurrently. This ensures that the application can scale efficiently and handle large volumes of data asynchronously.
3. **Strategy Pattern:** The application utilizes the Strategy Pattern to detect token swaps. The SwapDetectorService interface and its BasicSwapDetectorService implementation encapsulate different strategies for identifying swap transactions. You can easily extend the application to incorporate more advanced swap detection techniques by implementing new strategies without modifying the existing code.
4. **Factory Pattern:** The CachedAlchemyApiServiceBuilder class follows the Factory Pattern, creating instances of the CachedAlchemyApiService with different configurations. This approach makes it easier to manage dependencies (like APIs, cache layers, and executors) and ensures that the service is correctly configured without exposing the internal implementation.
5. **Caching:** The use of Caffeine provides an efficient caching mechanism to minimize redundant API calls and improve performance by storing frequently accessed data like balances and transfer histories. This reduces the number of external API calls, leading to better overall performance.
6. **Modular Design:** The design follows modular principles, where different services such as balance fetching, swap detection, and transfer processing are separated into distinct classes. This allows easy extensibility and maintenance of the code.

## Strengths

- **Stability:** The application has been designed to handle various Ethereum transaction scenarios, including token transfers and token swaps. Robust error handling ensures that the application remains stable even when API requests fail or data is missing.
- **Performance:** Asynchronous tasks and caching optimize the program’s performance by reducing the number of redundant API requests and efficiently managing network I/O.
- **Extensibility:** The design is modular, with separate services for fetching data, detecting swaps, and calculating balances. This allows for easy updates or additions to the functionality in the future (e.g., adding more swap detection logic or supporting different blockchain networks).
- **Clean Architecture:** The project follows a clean architecture approach by decoupling concerns into distinct modules, making it easier to maintain and extend.

## Future Work
- **Enhanced Swap Detection:** Currently, swaps are detected based on a static list of known DEX contract addresses. Future improvements could include dynamic detection of swap contracts or integrating a more sophisticated method of identifying swap transactions.
- **Optimized Data Retrieval:** Implement more intelligent data retrieval strategies, such as batch fetching or rate-limiting optimizations to reduce the number of API calls when monitoring multiple addresses.
- **Expanded Reporting:** Adding more detailed reporting features, such as transaction category breakdowns (e.g., internal vs. external transfers), or generating a CSV/JSON report for the user.
- **Multi-network Support:** Extend the project to support other blockchains, like Binance Smart Chain or Polygon, by adding respective APIs and modifying the existing logic.

## How to Compile and Run
### Prerequisites
- JDK 21 or higher installed on your system.
- Maven 3.8+ for building the project.

### Clone the Repository
```bash
git clone https://github.com/rbbozkurt/eth-monitor.git
cd eth-monitor
```
### Compile the Project

To compile the project, simply run the following command:
```bash
mvn compile
```

### Run the Application

To run the program, use Maven’s exec plugin. Replace the `<balancesApiKey>`, `<transfersApiKey>`, `<tokensApiKey>`, and `<pricesApiKey>` placeholders with actual API keys obtained from Alchemy.
```bash
mvn compile exec:java -Dexec.args="-a <ethereumAddress> -b <balancesApiKey> -f <transfersApiKey> -k <tokensApiKey> -p <pricesApiKey> -t <maxTransferCount>"
```
Where:

- `-a <ethereumAddress>`: Ethereum address to monitor.

- `-b <balancesApiKey>`: Alchemy API key for fetching token balances.

- `-f <transfersApiKey>`: Alchemy API key for fetching historical transfers.

- `-k <tokensApiKey>`: Alchemy API key for fetching token metadata.

- `-p <pricesApiKey>`: Alchemy API key for fetching token prices.

- `-t <maxTransferCount>`: The number of recent transfers to fetch.

**For example:**

```bash
mvn compile exec:java -Dexec.args="-a 0xF977814e90dA44bFA03b6295A0616a897441aceC -b <balancesApiKey> -f <transfersApiKey> -k <tokensApiKey> -p <pricesApiKey> -t 100"
```
The address **0xF977814e90dA44bFA03b6295A0616a897441aceC** is associated with Binance's hot wallet. This wallet holds significant amounts of Ethereum (ETH) and various ERC-20 tokens, serving as an address for Binance's internal operations.


### Example Output

Once the application has executed, it will display an analysis of the provided Ethereum address, including:

- Total number of transactions.
- Estimated swap count.
- Total transaction volume in USD.
- Total balance of all assets in USD.

Additionally, it will list the token balances, their values in USD, and detailed historical transfers.

## Demo
![ETH Monitor Demo Video](assets/eth_monitor_demo.gif)
**PS**: Please use your own Alchemy API Key, I have deleted the one used in demo. 
## Technologies Used

- **Alchemy API**: Blockchain data provider used to fetch token balances, transaction data, and more.
- **ExecutorService**: Used for handling asynchronous tasks for better scalability.
- **Caffeine Cache**: A high-performance caching library used to store and retrieve frequently used data.
- **Maven**: Build automation tool used for dependency management and project compilation.


## Conclusion

This project serves as a comprehensive solution for monitoring Ethereum addresses, analyzing transactions, and providing detailed reports on token balances and swap activities. It leverages modern Java libraries and best practices to provide a stable, performant, and extendable solution.

For future versions, the system could support additional features such as multi-network monitoring or deeper analysis capabilities.
Contact

For any questions or suggestions, feel free to reach out to me at [resitberkaybozkurt@gmail.com](mailto:resitberkaybozkurt@gmail.com).