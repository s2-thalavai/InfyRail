package com.infosys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "train")
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "train_name", nullable = false)
    private String trainName;

    @Column(name = "arrival_time", length = 8)
    private String arrivalTime;

    @Column(name = "departure_time", length = 8)
    private String departureTime;

    @Column(name = "fare")
    private Double fare;

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
}
