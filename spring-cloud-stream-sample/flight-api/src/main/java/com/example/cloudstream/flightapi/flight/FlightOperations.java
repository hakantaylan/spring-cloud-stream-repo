package com.example.cloudstream.flightapi.flight;

import com.example.cloudstream.flightapi.airport.Airport;

import java.util.Optional;

public interface FlightOperations {
    String create(Flight flight);

    Optional<Flight> findById(String id);

    Optional<Flight> findConfirmedFlightByPlaneId(String planeId);

    void flightArrivedIn(String flightId, Airport airport);
}
