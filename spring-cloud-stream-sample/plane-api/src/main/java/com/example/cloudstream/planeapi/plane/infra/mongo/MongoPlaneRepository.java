package com.example.cloudstream.planeapi.plane.infra.mongo;

import com.example.cloudstream.planeapi.plane.Plane;
import com.example.cloudstream.planeapi.plane.PlaneRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
class MongoPlaneRepository implements PlaneRepository {

    private final PlaneMapper planeMapper;
    private final SpringPlaneRepository springPlaneRepository;

    @Override
    public String save(Plane plane) {
        PlaneDocument savedPlane =
                springPlaneRepository.save(planeMapper.toPlaneDocument(plane));
        return savedPlane.getId();
    }

    @Override
    public Optional<Plane> findById(String id) {
        return springPlaneRepository.findById(id)
                .map(planeMapper::toPlane);
    }
}
