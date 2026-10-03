package com.infosys.dto;

import com.infosys.entity.Train;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class TrainDTO {

    /** 24-hour time in HH:mm:ss format, e.g. 13:30:00 */
    private static final String TIME_PATTERN = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";

    private Integer id;

    @NotBlank(message = "{train.trainname.notpresent}")
    private String trainName;

    @NotBlank(message = "{train.arrivaltime.notpresent}")
    @Pattern(regexp = TIME_PATTERN, message = "{train.arrivaltime.invalid}")
    private String arrivalTime;

    @NotBlank(message = "{train.departuretime.notpresent}")
    @Pattern(regexp = TIME_PATTERN, message = "{train.departuretime.invalid}")
    private String departureTime;

    @NotNull(message = "{train.fare.notpresent}")
    @Positive(message = "{train.fare.invalid}")
    private Double fare;

    public static TrainDTO fromEntity(Train train) {
        TrainDTO dto = new TrainDTO();
        dto.setId(train.getId());
        dto.setTrainName(train.getTrainName());
        dto.setArrivalTime(train.getArrivalTime());
        dto.setDepartureTime(train.getDepartureTime());
        dto.setFare(train.getFare());
        return dto;
    }

    /** Copies the editable details (everything except id) onto the given entity. */
    public void copyTo(Train train) {
        train.setTrainName(this.trainName);
        train.setArrivalTime(this.arrivalTime);
        train.setDepartureTime(this.departureTime);
        train.setFare(this.fare);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }
    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public Double getFare() { return fare; }
    public void setFare(Double fare) { this.fare = fare; }

    @Override
    public String toString() {
        return "TrainDTO [id=" + id + ", trainName=" + trainName + ", arrivalTime=" + arrivalTime
                + ", departureTime=" + departureTime + ", fare=" + fare + "]";
    }
}
