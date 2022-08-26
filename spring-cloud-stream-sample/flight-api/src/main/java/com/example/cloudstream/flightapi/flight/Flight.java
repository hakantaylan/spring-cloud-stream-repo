package com.example.cloudstream.flightapi.flight;

import com.example.cloudstream.flightapi.airport.Airport;
import com.example.cloudstream.flightapi.plane.Plane;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.example.cloudstream.flightapi.flight.FlightStatus.FINISHED;


@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Flight {
    private final String id;
    private final Plane plane;
    private final Airport origin;
    private final Airport destination;
    private final FlightStatus status;

    public Flight arrivedIn(Airport airport) {
        if (airport.equals(destination)) {
            return this.toBuilder().status(FINISHED).build();
        }
        return this;
    }
}
