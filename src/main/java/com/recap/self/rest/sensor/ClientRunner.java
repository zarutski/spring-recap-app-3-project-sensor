package com.recap.self.rest.sensor;

import com.recap.self.rest.sensor.response.MeasurementDTO;
import com.recap.self.rest.sensor.service.RestClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ClientRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ClientRunner.class);

    private final RestClientService clientService;
    private final ExecutorService executorService;

    @Autowired
    public ClientRunner(RestClientService clientService) {
        this.clientService = clientService;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public void run(String... args) throws Exception {
        String sensorName = "tls-5x7";
        if (clientService.registerSensor(sensorName)) {
            clientService.sendSensorMeasurements(sensorName, executorService);
        }
        printMeasurementsData();
        executorService.shutdown();
    }

    public void printMeasurementsData() {
        List<MeasurementDTO> measurements = clientService.retrieveMeasurements();
        measurements.forEach(m -> logger.warn(m.toString()));
        logger.warn("Retrieved data size: {}", measurements.size());
    }

}
