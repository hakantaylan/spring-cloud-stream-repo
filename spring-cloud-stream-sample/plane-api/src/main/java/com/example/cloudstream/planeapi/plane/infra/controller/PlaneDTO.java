package com.example.cloudstream.planeapi.plane.infra.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.example.cloudstream.planeapi.airport.Airport;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotEmpty;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
class PlaneDTO {

    @NotEmpty
    private final String code;
    @NotEmpty
    private final String type;

    private final Airport airport;
}
