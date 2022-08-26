package com.example.cloudstream.planeapi.plane.infra.stream;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class PlaneArrived {
    private String planeId;
    private String currentAirport;
}
