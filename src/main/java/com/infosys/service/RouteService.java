package com.infosys.service;

import java.util.List;

import com.infosys.dto.RouteDTO;
import com.infosys.dto.TrainDTO;
import com.infosys.exception.InfyRailException;

public interface RouteService {

    Integer createRoute(RouteDTO routeDTO) throws InfyRailException;

    RouteDTO getRoute(Integer routeId) throws InfyRailException;

    List<RouteDTO> getTrainsBySourceAndDestination(String source, String destination) throws InfyRailException;

    RouteDTO updateRoute(Integer routeId, String source, String destination) throws InfyRailException;

    void deleteTrainFromRoute(Integer routeId, Integer trainId) throws InfyRailException;

    /** @return true if the train was newly added to the route, false if an existing one was updated */
    boolean updateTrainInRoute(Integer routeId, TrainDTO trainDTO) throws InfyRailException;
}
