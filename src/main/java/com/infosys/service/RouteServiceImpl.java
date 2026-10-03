package com.infosys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infosys.dto.RouteDTO;
import com.infosys.dto.TrainDTO;
import com.infosys.entity.Route;
import com.infosys.entity.Train;
import com.infosys.exception.InfyRailException;
import com.infosys.repository.RouteRepository;
import com.infosys.repository.TrainRepository;

@Service("routeService")
@Transactional(rollbackFor = InfyRailException.class)
public class RouteServiceImpl implements RouteService {

    private static final int MAX_ROUTE_ID = 999;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Override
    public Integer createRoute(RouteDTO routeDTO) throws InfyRailException {
        Integer maxRouteId = routeRepository.findMaxRouteId();
        if (maxRouteId != null && maxRouteId >= MAX_ROUTE_ID) {
            throw new InfyRailException("Service.ROUTE_ID_LIMIT_EXCEEDED");
        }

        Route route = new Route();
        route.setSource(routeDTO.getSource());
        route.setDestination(routeDTO.getDestination());

        List<Train> trains = new ArrayList<>();
        for (TrainDTO trainDTO : routeDTO.getTrainList()) {
            trains.add(resolveTrain(trainDTO));
        }
        route.setTrainList(trains);

        return routeRepository.save(route).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public RouteDTO getRoute(Integer routeId) throws InfyRailException {
        return RouteDTO.fromEntity(findRoute(routeId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteDTO> getTrainsBySourceAndDestination(String source, String destination)
            throws InfyRailException {
        List<RouteDTO> routes = routeRepository
                .findBySourceIgnoreCaseAndDestinationIgnoreCase(source, destination).stream()
                .filter(route -> !route.getTrainList().isEmpty())
                .map(RouteDTO::fromEntity)
                .collect(Collectors.toList());
        if (routes.isEmpty()) {
            throw new InfyRailException("Service.NO_TRAINS_FOUND");
        }
        return routes;
    }

    @Override
    public RouteDTO updateRoute(Integer routeId, String source, String destination) throws InfyRailException {
        if (source == null && destination == null) {
            throw new InfyRailException("Service.ROUTE_UPDATE_DETAILS_MISSING");
        }
        Route route = findRoute(routeId);
        if (source != null) {
            route.setSource(source);
        }
        if (destination != null) {
            route.setDestination(destination);
        }
        return RouteDTO.fromEntity(route); // managed entity -> changes flushed on commit
    }

    @Override
    public void deleteTrainFromRoute(Integer routeId, Integer trainId) throws InfyRailException {
        Route route = findRoute(routeId);
        boolean removed = route.getTrainList().removeIf(train -> train.getId().equals(trainId));
        if (!removed) {
            throw new InfyRailException("Service.TRAIN_NOT_IN_ROUTE");
        }
    }

    @Override
    public boolean updateTrainInRoute(Integer routeId, TrainDTO trainDTO) throws InfyRailException {
        Route route = findRoute(routeId);
        Train train = resolveTrain(trainDTO);
        boolean alreadyInRoute = route.getTrainList().stream()
                .anyMatch(existing -> existing.getId().equals(train.getId()));
        if (!alreadyInRoute) {
            route.getTrainList().add(train);
        }
        return !alreadyInRoute;
    }

    private Route findRoute(Integer routeId) throws InfyRailException {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new InfyRailException("Service.ROUTEID_NOT_FOUND"));
    }

    /**
     * id == null -> a new train is created.
     * id != null -> the existing train is loaded and its details are updated
     *               (throws TRAIN_NOT_FOUND if it does not exist).
     */
    private Train resolveTrain(TrainDTO trainDTO) throws InfyRailException {
        Train train;
        if (trainDTO.getId() == null) {
            train = new Train();
        } else {
            train = trainRepository.findById(trainDTO.getId())
                    .orElseThrow(() -> new InfyRailException("Service.TRAIN_NOT_FOUND"));
        }
        trainDTO.copyTo(train);
        return trainRepository.save(train);
    }
}
