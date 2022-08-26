package com.example.cloudstream.planeapi.plane.infra.stream;

public interface PlaneArrivedSender {

    void send(PlaneArrived planeEvent);

}
