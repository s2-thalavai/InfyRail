package com.infosys.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "route")
public class Route {

    // AUTO_INCREMENT continues from the seeded ids (101..103), so new routes get 104, 105 ...
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String destination;

    // The "trainList" of a route is modelled as a foreign key (route_id) in the train table.
    // Removing a train from this list only unlinks it from the route (route_id -> NULL).
    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "route_id")
    private List<Train> trainList = new ArrayList<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public List<Train> getTrainList() { return trainList; }
    public void setTrainList(List<Train> trainList) { this.trainList = trainList; }
}
