package com.example.cloudstream.planeapi.plane;

import java.util.Optional;

public interface PlaneRepository {
    String save(Plane plane);

    Optional<Plane> findById(String id);
}
