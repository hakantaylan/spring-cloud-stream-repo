package com.example.cloudstream.flightapi.flight;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.cloudstream.flightapi.TestData.*;
import static com.example.cloudstream.flightapi.flight.FlightStatus.FINISHED;
import static com.example.cloudstream.flightapi.flight.FlightStatus.CONFIRMED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightFacadeTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightNotifications flightNotifications;

    @InjectMocks
    private FlightFacade flightFacade;

    @Captor
    private ArgumentCaptor<Flight> flightArgumentCaptor;

    private final static String CREATED_ID = "id";

    @Test
    void shouldSaveSetFlightToConfirmedAndSaveAFlight() {
        when(flightRepository.save(FLIGHT)).thenReturn(CREATED_ID);

        String id = flightFacade.create(FLIGHT.toBuilder().status(null).build());

        assertThat(id).isEqualTo(CREATED_ID);
        verify(flightRepository).save(FLIGHT);
    }

    @Test
    void shouldFindConfirmedFlightByPlaneIdWhenThereIsOneFlight() {
        String id = "id1";
        when(flightRepository.findConfirmedFlightsByPlaneId(id)).thenReturn(List.of(FLIGHT));

        Optional<Flight> optionalFlight = flightFacade.findConfirmedFlightByPlaneId(id);

        assertThat(optionalFlight).isPresent();
        Flight flight = optionalFlight.get();
        assertThat(flight).isEqualTo(FLIGHT);
    }

    @Test
    void shouldFindConfirmedFlightAndReturnEmptyWhenIsOneFlight() {
        String id = "id1";
        when(flightRepository.findConfirmedFlightsByPlaneId(id)).thenReturn(new ArrayList<>());

        Optional<Flight> optionalFlight = flightFacade.findConfirmedFlightByPlaneId(id);

        assertThat(optionalFlight).isNotPresent();
    }

    @Test
    void shouldFindConfirmedFlightsAndThrowIllegalStateWhenAreMoreThanOneFlight() {
        String id = "id1";
        when(flightRepository.findConfirmedFlightsByPlaneId(id)).thenReturn(List.of(FLIGHT, FLIGHT));

        assertThrows(IllegalStateException.class,
                () -> flightFacade.findConfirmedFlightByPlaneId(id));
    }

    @Test
    void shouldSaveAnArrivedFlightWhenFlightArrivesInDestinationAndSendANotification() {
        when(flightRepository.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT_WITH_ID));

        flightFacade.flightArrivedIn(FLIGHT_ID, CNH);

        verify(flightRepository).findById(FLIGHT_ID);
        verify(flightRepository).save(flightArgumentCaptor.capture());

        Flight savedFlight = flightArgumentCaptor.getValue();
        assertThat(savedFlight.getStatus()).isEqualTo(FINISHED);
        assertThat(savedFlight.getId()).isEqualTo(FLIGHT_ID);

        verify(flightNotifications).flightFinished(FLIGHT_ID);
    }

    @Test
    void shouldNotSetStatusToArrivedWhenItArrivesInAnotherAirport() {
        when(flightRepository.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT_WITH_ID));

        flightFacade.flightArrivedIn(FLIGHT_ID, POA);

        verify(flightRepository).findById(FLIGHT_ID);
        verify(flightRepository).save(flightArgumentCaptor.capture());

        Flight savedFlight = flightArgumentCaptor.getValue();
        assertThat(savedFlight.getStatus()).isEqualTo(CONFIRMED);
        assertThat(savedFlight.getId()).isEqualTo(FLIGHT_ID);

        verifyNoInteractions(flightNotifications);
    }

    @Test
    void shouldThrowFlightNotFoundExceptionWhenFlightDoesNotExist() {
        when(flightRepository.findById(FLIGHT_ID)).thenReturn(Optional.empty());

        var exception = assertThrows(FlightNotFoundException.class,
                () -> flightFacade.flightArrivedIn(FLIGHT_ID, POA));

        verify(flightRepository).findById(FLIGHT_ID);
        verifyNoMoreInteractions(flightRepository);

        assertThat(exception.getMessage()).contains(FLIGHT_ID);

        verifyNoInteractions(flightNotifications);
    }

    @Test
    void shouldFindAFlightById() {
        when(flightRepository.findById(FLIGHT_ID)).thenReturn(Optional.of(FLIGHT));

        Optional<Flight> optionalFlight = flightFacade.findById(FLIGHT_ID);

        assertThat(optionalFlight).hasValue(FLIGHT);
        verify(flightRepository).findById(FLIGHT_ID);
    }
}
