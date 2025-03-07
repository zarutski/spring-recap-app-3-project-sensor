package com.recap.self.rest.sensor.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.recap.self.rest.sensor.constants.DataConstants.PARAM_NAME;
import static com.recap.self.rest.sensor.constants.DataConstants.PARAM_RAINING;
import static com.recap.self.rest.sensor.constants.DataConstants.PARAM_SENSOR;
import static com.recap.self.rest.sensor.constants.DataConstants.PARAM_VALUE;

@Service
public class DataGenerationService {

    private final Random random = new Random();

    public List<Map<String, Object>> generateSensorRequests(String sensorName, int size) {
        List<Map<String, Object>> arrayList = new ArrayList<>(size);
        Map<String, Object> sensorJson = buildSensorJson(sensorName);
        for (int i = 0; i < size; i++) {
            arrayList.add(generateMeasurementRequest(sensorJson));
        }
        return arrayList;
    }

    private Map<String, Object> generateMeasurementRequest(Map<String, Object> sensorJson) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(PARAM_VALUE, random.nextDouble(-100, 100));
        requestBody.put(PARAM_RAINING, random.nextBoolean());
        requestBody.put(PARAM_SENSOR, sensorJson);
        return requestBody;
    }

    private Map<String, Object> buildSensorJson(String sensorName) {
        Map<String, Object> sensor = new HashMap<>();
        sensor.put(PARAM_NAME, sensorName);
        return sensor;
    }
}
