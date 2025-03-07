package com.recap.self.rest.sensor.connection;

import com.recap.self.rest.sensor.response.MeasurementDTO;
import com.recap.self.rest.sensor.exception.RestClientException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.recap.self.rest.sensor.constants.DataConstants.PARAM_NAME;

@Component
public class SimpleRestClient {

    private static final String SENSORS_REGISTRATION_URL = "http://localhost:8080/sensors/registration";
    private static final String MEASUREMENTS_ADD_URL = "http://localhost:8080/measurements/add";
    private static final String MEASUREMENTS_URL = "http://localhost:8080/measurements";

    private final RestTemplate restTemplate;

    public SimpleRestClient() {
        this.restTemplate = new RestTemplate();
    }

    public ResponseEntity<String> processSensorRegistration(String sensorName) {
        try {
            return restTemplate.postForEntity(SENSORS_REGISTRATION_URL, formSensorRequest(sensorName), String.class);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            throw new RestClientException(exception.getStatusCode(), exception.getResponseBodyAsString());
        }
    }

    public ResponseEntity<List<MeasurementDTO>> getMeasurements() {
        try {
            return restTemplate.exchange(MEASUREMENTS_URL, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
            });
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            throw new RestClientException(exception.getStatusCode(), exception.getResponseBodyAsString());
        }
    }

    public ResponseEntity<String> saveMeasurements(Map<String, Object> request) {
        try {
            return restTemplate.postForEntity(MEASUREMENTS_ADD_URL, new HttpEntity<>(request), String.class);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            throw new RestClientException(exception.getStatusCode(), exception.getResponseBodyAsString());
        }
    }

    private HttpEntity<Map<String, String>> formSensorRequest(String sensorName) {
        Map<String, String> jsonToSent = new HashMap<>();
        jsonToSent.put(PARAM_NAME, sensorName);
        return new HttpEntity<>(jsonToSent);
    }
}
