package com.example.cloudstream.flightapi.flight;

import java.util.List;
import java.util.Optional;

public interface FlightRepository {
    String save(Flight flight);

    List<Flight> findConfirmedFlightsByPlaneId(String planeId);

    Optional<Flight> findById(String flightId);
}
