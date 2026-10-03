package com.infosys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infosys.entity.Route;

public interface RouteRepository extends JpaRepository<Route, Integer> {

    List<Route> findBySourceIgnoreCaseAndDestinationIgnoreCase(String source, String destination);

    @Query("SELECT MAX(r.id) FROM Route r")
    Integer findMaxRouteId();
}
