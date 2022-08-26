package com.example.cloudstream.planeapi.plane.infra.mongo;

import com.example.cloudstream.planeapi.plane.Plane;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface PlaneMapper {

    PlaneDocument toPlaneDocument(Plane plane);

    Plane toPlane(PlaneDocument plane);
}
