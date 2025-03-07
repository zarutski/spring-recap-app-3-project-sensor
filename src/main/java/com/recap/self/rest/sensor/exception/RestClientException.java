package com.recap.self.rest.sensor.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class RestClientException extends HttpStatusCodeException {

    private final String errorMessage;

    public RestClientException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
        this.errorMessage = statusText;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
