package com.infosys.service;

import com.infosys.dto.TrainDTO;
import com.infosys.exception.InfyRailException;

public interface TrainService {

    Integer createTrain(TrainDTO trainDTO) throws InfyRailException;

    void updateFare(Integer trainId, Double fare) throws InfyRailException;
}
