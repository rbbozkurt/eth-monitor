package com.rbbozkurt.ethmonitor.factory;

import com.rbbozkurt.ethmonitor.client.interfaces.HttpClient;

/**
 * Factory interface for creating instances of {@link HttpClient}.
 * This interface defines a method for creating an {@link HttpClient} based on the provided base URL.
 */
public interface HttpClientFactory {

    /**
     * Creates an instance of {@link HttpClient} for the specified base URL.
     *
     * @param baseUrl the base URL for the HTTP client
     * @return an instance of {@link HttpClient}
     */
    HttpClient getClient(String baseUrl);
}
