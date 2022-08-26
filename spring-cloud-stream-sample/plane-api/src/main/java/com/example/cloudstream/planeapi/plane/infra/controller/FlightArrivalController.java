package com.example.cloudstream.planeapi.plane.infra.controller;

import com.example.cloudstream.planeapi.airport.Airport;
import com.example.cloudstream.planeapi.plane.PlaneOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flight-arrivals")
public class FlightArrivalController {

    private final PlaneOperations planeOperations;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void flightArrived(@RequestBody FlightArrival flightArrival) {
        planeOperations.arrivedIn(flightArrival.getPlaneId(),
                new Airport(flightArrival.getAirport()));
    }

}
