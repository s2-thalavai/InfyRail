package com.infosys.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.dto.RouteDTO;
import com.infosys.dto.TrainDTO;
import com.infosys.exception.InfyRailException;
import com.infosys.service.RouteService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping("/routes")
@CrossOrigin
@Validated
public class RouteAPI {

    private static final Log LOGGER = LogFactory.getLog(RouteAPI.class);

    @Autowired
    private RouteService routeService;

    @Autowired
    private Environment environment;

    // 1. Create a route  ->  POST /routes
    @PostMapping
    public ResponseEntity<Integer> createRoute(@Valid @RequestBody RouteDTO routeDTO) throws InfyRailException {
        Integer routeId = routeService.createRoute(routeDTO);
        LOGGER.info("Route created with id " + routeId);
        return new ResponseEntity<>(routeId, HttpStatus.CREATED);
    }

    // 2. View route details  ->  GET /routes/{routeId}
    @GetMapping("/{routeId}")
    public ResponseEntity<RouteDTO> getRoute(
            @PathVariable @Min(value = 100, message = "{route.routeid.invalid}")
            @Max(value = 999, message = "{route.routeid.invalid}") Integer routeId) throws InfyRailException {
        return new ResponseEntity<>(routeService.getRoute(routeId), HttpStatus.OK);
    }

    // 3. View trains by source and destination  ->  GET /routes/trains?source=&destination=
    @GetMapping("/trains")
    public ResponseEntity<List<RouteDTO>> getTrainsBySourceAndDestination(
            @RequestParam @NotBlank(message = "{route.source.notpresent}")
            @Pattern(regexp = "^[A-Za-z]+$", message = "{route.source.invalid}") String source,
            @RequestParam @NotBlank(message = "{route.destination.notpresent}")
            @Pattern(regexp = "^[A-Za-z]+$", message = "{route.destination.invalid}") String destination)
            throws InfyRailException {
        return new ResponseEntity<>(routeService.getTrainsBySourceAndDestination(source, destination),
                HttpStatus.OK);
    }

    // 4. Update route using matrix params  ->  PUT /routes/{routeId};source=X;destination=Y  (no body)
    @PutMapping("/{routeId}")
    public ResponseEntity<RouteDTO> updateRoute(
            @PathVariable @Min(value = 100, message = "{route.routeid.invalid}")
            @Max(value = 999, message = "{route.routeid.invalid}") Integer routeId,
            @MatrixVariable(required = false)
            @Pattern(regexp = "^[A-Za-z]+$", message = "{route.source.invalid}") String source,
            @MatrixVariable(required = false)
            @Pattern(regexp = "^[A-Za-z]+$", message = "{route.destination.invalid}") String destination)
            throws InfyRailException {
        RouteDTO updated = routeService.updateRoute(routeId, source, destination);
        LOGGER.info("Route " + routeId + " updated");
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // 5. Delete a train from a route  ->  DELETE /routes/{routeId}/{trainId}
    @DeleteMapping("/{routeId}/{trainId}")
    public ResponseEntity<String> deleteTrainFromRoute(
            @PathVariable @Min(value = 100, message = "{route.routeid.invalid}")
            @Max(value = 999, message = "{route.routeid.invalid}") Integer routeId,
            @PathVariable Integer trainId) throws InfyRailException {
        routeService.deleteTrainFromRoute(routeId, trainId);
        String message = environment.getProperty("API.TRAIN_DELETED_FROM_ROUTE") + " " + routeId;
        LOGGER.info(message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // 6. Add / update train details in an existing route  ->  PUT /routes/{routeId}  (JSON body)
    //    Same URL as #4: Spring routes here because the request carries an application/json body.
    @PutMapping(value = "/{routeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTrainInRoute(
            @PathVariable @Min(value = 100, message = "{route.routeid.invalid}")
            @Max(value = 999, message = "{route.routeid.invalid}") Integer routeId,
            @Valid @RequestBody TrainDTO trainDTO) throws InfyRailException {
        boolean added = routeService.updateTrainInRoute(routeId, trainDTO);
        String key = added ? "API.TRAIN_ADDED_TO_ROUTE" : "API.TRAIN_UPDATED_IN_ROUTE";
        String message = environment.getProperty(key) + " " + routeId;
        LOGGER.info(message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
