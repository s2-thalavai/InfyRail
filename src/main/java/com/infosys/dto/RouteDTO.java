package com.infosys.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.infosys.entity.Route;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class RouteDTO {

    private Integer id;

    @NotBlank(message = "{route.source.notpresent}")
    @Pattern(regexp = "^[A-Za-z]+$", message = "{route.source.invalid}")
    private String source;

    @NotBlank(message = "{route.destination.notpresent}")
    @Pattern(regexp = "^[A-Za-z]+$", message = "{route.destination.invalid}")
    private String destination;

    @NotEmpty(message = "{route.trainlist.notpresent}")
    private List<@Valid TrainDTO> trainList;

    public static RouteDTO fromEntity(Route route) {
        RouteDTO dto = new RouteDTO();
        dto.setId(route.getId());
        dto.setSource(route.getSource());
        dto.setDestination(route.getDestination());
        dto.setTrainList(route.getTrainList().stream()
                .map(TrainDTO::fromEntity)
                .collect(Collectors.toList()));
        return dto;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public List<TrainDTO> getTrainList() { return trainList; }
    public void setTrainList(List<TrainDTO> trainList) { this.trainList = trainList; }

    @Override
    public String toString() {
        return "RouteDTO [id=" + id + ", source=" + source + ", destination=" + destination
                + ", trainList=" + trainList + "]";
    }
}
