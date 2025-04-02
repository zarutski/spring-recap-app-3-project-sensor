package com.recap.self.rest.sensor.service;

import com.recap.self.rest.sensor.connection.SimpleRestClient;
import com.recap.self.rest.sensor.exception.RestClientException;
import com.recap.self.rest.sensor.response.MeasurementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class RestClientService {

    private static final Logger logger = LoggerFactory.getLogger(RestClientService.class);

    private final SimpleRestClient restClient;
    private final DataGenerationService generationService;

    @Autowired
    public RestClientService(SimpleRestClient restClient, DataGenerationService generationService) {
        this.restClient = restClient;
        this.generationService = generationService;
    }

    public boolean registerSensor(String name) {
        try {
            ResponseEntity<String> response = restClient.processSensorRegistration(name);
            logger.warn("Registered: {} ; Status code: {}", response.getBody(), response.getStatusCode());
        } catch (RestClientException ex) {
            logger.warn(ex.getErrorMessage());
            return false;
        }
        return true;
    }

    public void sendSensorMeasurements(String name, ExecutorService executorService) {
        try {
            for (Map<String, Object> request : generationService.generateSensorRequests(name, 1000)) {
                executorService.submit(() -> restClient.saveMeasurements(request));
            }
            waitForProcessing(executorService);
        } catch (RestClientException ex) {
            logger.warn(ex.getErrorMessage());
        }
    }

    public List<MeasurementDTO> retrieveMeasurements() {
        ResponseEntity<List<MeasurementDTO>> response = restClient.getMeasurements();
        logger.warn("Data fetched - status code: {}", response.getStatusCode());
        return parseMeasurementsResponse(response);
    }

    private List<MeasurementDTO> parseMeasurementsResponse(ResponseEntity<List<MeasurementDTO>> response) {
        List<MeasurementDTO> measurements = response.getBody();
        if (measurements != null) {
            return measurements;
        }
        return Collections.emptyList();
    }

    private void waitForProcessing(ExecutorService executorService) {
        try {
            executorService.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.warn(e.getMessage());
        }
    }
}
