package com.example.cloudstream.planeapi.plane;

import com.example.cloudstream.planeapi.airport.Airport;
import com.example.cloudstream.planeapi.plane.infra.stream.PlaneArrived;
import com.example.cloudstream.planeapi.plane.infra.stream.PlaneArrivedSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
class PlaneFacade implements PlaneOperations {

    private final PlaneRepository planeRepository;
    private final PlaneArrivedSender planeEventSender;

    @Override
    public String create(Plane plane) {
        return planeRepository.save(plane);
    }

    @Override
    public Optional<Plane> findById(String id) {
        return planeRepository.findById(id);
    }

    @Override
    public void arrivedIn(String planeId, Airport airport) {
        Plane plane = planeRepository.findById(planeId).orElseThrow(PlaneNotFoundException::new);
        planeRepository.save(plane.toBuilder().airport(airport).build());

        planeEventSender.send(PlaneArrived.builder().planeId(planeId).currentAirport(airport.getCode()).build());
    }
}
