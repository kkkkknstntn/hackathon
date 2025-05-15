package org.ru.backend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ml-service", url = "${ml.service.url}")
public interface MLClient {

    @PostMapping("/predict")
    String getPrediction(@RequestBody String inputData);  // Example request, replace with actual data structure

}