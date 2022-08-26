package com.example.cloudstream.flightapi.flight.infra.mongo;

import com.example.cloudstream.flightapi.flight.FlightStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

interface SpringFlightRepository extends MongoRepository<FlightDocument, String> {
    List<FlightDocument> findByPlaneIdAndStatus(String planeId, FlightStatus status);
}
