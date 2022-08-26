package com.example.cloudstream.planeapi.plane;

import com.example.cloudstream.planeapi.airport.Airport;

import java.util.Optional;

public interface PlaneOperations {
    String create(Plane plane);

    Optional<Plane> findById(String id);

    void arrivedIn(String planeId, Airport airport);
}
