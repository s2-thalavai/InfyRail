package com.infosys.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.dto.TrainDTO;
import com.infosys.exception.InfyRailException;
import com.infosys.service.TrainService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/trains")
@CrossOrigin
@Validated
public class TrainAPI {

    private static final Log LOGGER = LogFactory.getLog(TrainAPI.class);

    @Autowired
    private TrainService trainService;

    @Autowired
    private Environment environment;

    // 7. Create train details  ->  POST /trains
    @PostMapping
    public ResponseEntity<Integer> createTrain(@Valid @RequestBody TrainDTO trainDTO) throws InfyRailException {
        Integer trainId = trainService.createTrain(trainDTO);
        LOGGER.info("Train created with id " + trainId);
        return new ResponseEntity<>(trainId, HttpStatus.CREATED);
    }

    // 8. Update fare  ->  PUT /trains/{trainId}   raw JSON body, e.g. 1050.75
    @PutMapping("/{trainId}")
    public ResponseEntity<String> updateFare(@PathVariable Integer trainId,
            @RequestBody @NotNull(message = "{train.fare.notpresent}")
            @Positive(message = "{train.fare.invalid}") Double fare) throws InfyRailException {
        trainService.updateFare(trainId, fare);
        String message = environment.getProperty("API.FARE_UPDATED") + " " + trainId;
        LOGGER.info(message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
