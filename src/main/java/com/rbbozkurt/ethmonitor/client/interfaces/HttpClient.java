package com.rbbozkurt.ethmonitor.client.interfaces;

import java.io.IOException;

/**
 * Interface for an HTTP client that supports making HTTP POST and GET requests.
 * This interface provides methods for sending requests with dynamic request bodies
 * and URLs, and handling the response by deserializing it into the specified type.
 */
public interface HttpClient {

    /**
     * Sends an HTTP POST request with a JSON body and deserializes the response
     * into the specified response type.
     *
     * @param jsonBody the request body in JSON format
     * @param responseType the class to deserialize the response into
     * @param <T> the type of the response object
     * @return the response deserialized to the specified type
     * @throws IOException if the request fails or the response cannot be parsed
     */
    <T> T post(String jsonBody, Class<T> responseType) throws IOException;

    /**
     * Sends an HTTP GET request to the specified URL and deserializes the response
     * into the specified response type.
     *
     * @param url the full URL to send the GET request to
     * @param responseType the class to deserialize the response into
     * @param <T> the type of the response object
     * @return the response deserialized to the specified type
     * @throws IOException if the request fails or the response cannot be parsed
     */
    <T> T get(String url, Class<T> responseType) throws IOException;
}
