package com.rbbozkurt.ethmonitor.client.impls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbbozkurt.ethmonitor.client.interfaces.HttpClient;
import okhttp3.*;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * An implementation of {@link HttpClient} using OkHttp to perform HTTP requests
 * against the Alchemy API. This class supports both GET and POST requests.
 */
public class AlchemyHttpClient implements HttpClient {

    private static final Logger logger = Logger.getLogger(AlchemyHttpClient.class.getName());
    private static final MediaType JSON = MediaType.get("application/json");
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl;

    /**
     * Constructs a new {@code AlchemyHttpClient} with the specified base URL.
     *
     * @param baseUrl the base URL to use for requests
     */
    public AlchemyHttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Executes an HTTP POST request with a JSON body and deserializes the response.
     *
     * @param jsonBody the request body as a JSON string
     * @param responseType the class to deserialize the response into
     * @param <T> the type of the response object
     * @return the response deserialized to the specified type
     * @throws IOException if the request fails or the response cannot be parsed
     */
    @Override
    public <T> T post(String jsonBody, Class<T> responseType) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl)
                .post(RequestBody.create(jsonBody, JSON))
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (response.body() != null) {
                String responseBody = response.body().string();

                if (!response.isSuccessful()) {
                    throw new IOException("HTTP POST request failed: " + response.code());
                }

                return objectMapper.readValue(responseBody, responseType);
            } else {
                throw new IOException("HTTP POST response body is null");
            }
        }
    }

    /**
     * Executes an HTTP GET request and deserializes the response.
     *
     * @param url the full URL to send the GET request to
     * @param responseType the class to deserialize the response into
     * @param <T> the type of the response object
     * @return the response deserialized to the specified type
     * @throws IOException if the request fails or the response cannot be parsed
     */
    @Override
    public <T> T get(String url, Class<T> responseType) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (response.body() != null) {
                String responseBody = response.body().string();

                if (!response.isSuccessful()) {
                    throw new IOException("HTTP GET request failed: " + response.code());
                }

                return objectMapper.readValue(responseBody, responseType);
            } else {
                throw new IOException("HTTP GET response body is null");
            }
        }
    }
}
