package com.infosys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infosys.entity.Train;

public interface TrainRepository extends JpaRepository<Train, Integer> {
}
