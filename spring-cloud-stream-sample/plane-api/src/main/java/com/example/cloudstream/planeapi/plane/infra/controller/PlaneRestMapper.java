package com.example.cloudstream.planeapi.plane.infra.controller;

import com.example.cloudstream.planeapi.plane.Plane;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface PlaneRestMapper {
    Plane toPlane(PlaneDTO planeDTO);

    PlaneDTO toPlaneDTO(Plane planeDTO);
}
