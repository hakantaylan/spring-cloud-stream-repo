package com.example.cloudstream.flightapi.plane.infra.stream;

import com.example.cloudstream.flightapi.flight.FlightOperations;
import com.example.cloudstream.flightapi.messaging.utils.EmbeddedKafkaWithTopics;
import com.example.cloudstream.flightapi.messaging.utils.KafkaTestUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.util.Optional;

import static com.example.cloudstream.flightapi.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EmbeddedKafkaWithTopics
class PlaneEventProcessorTest {

    @Autowired
    private EmbeddedKafkaBroker broker;

    @MockBean
    private FlightOperations flightOperations;

    private KafkaTestUtils kafkaTestUtils;

    @BeforeEach
    void prepare() {
        this.kafkaTestUtils = new KafkaTestUtils(broker);
    }

    @Test
    void shouldTransformPlaneEventToFlightEvent() throws JSONException, InterruptedException {
        when(flightOperations.findConfirmedFlightByPlaneId(PLANE_ID)).
                thenReturn(Optional.of(FLIGHT_WITH_ID));

        Consumer<String, String> consumer = kafkaTestUtils.createConsumer("flight-arrived-v1");

        String planeEvent = new JSONObject().put("planeId", PLANE_ID)
                .put("currentAirport", CNH_CODE).toString();

        kafkaTestUtils.sendMessage("plane-arrived-v1", planeEvent);

        ConsumerRecord<String, String> record = kafkaTestUtils.getLastRecord(consumer, "flight-arrived-v1");

        var jsonFlightEvent = new JSONObject(record.value());
        assertThat(jsonFlightEvent.get("currentAirport")).isEqualTo(CNH_CODE);
        assertThat(jsonFlightEvent.get("flightId")).isEqualTo(FLIGHT_ID);

        assertThat(record.key()).isEqualTo(FLIGHT_ID);
    }

    @Test
    void shouldSendToDeadLetterWhenThereIsNoFlight() throws JSONException {
        when(flightOperations.findConfirmedFlightByPlaneId(PLANE_ID)).
                thenReturn(Optional.empty());

        Consumer<String, String> consumer = kafkaTestUtils.createConsumer("plane-arrived-dlq-v1");

        String planeEvent = new JSONObject().put("planeId", PLANE_ID)
                .put("currentAirport", CNH_CODE).toString();

        kafkaTestUtils.sendMessage("plane-arrived-v1", planeEvent);

        ConsumerRecord<String, String> record = kafkaTestUtils.getLastRecord(consumer, "plane-arrived-dlq-v1");

        var jsonFlightEvent = new JSONObject(record.value());
        assertThat(jsonFlightEvent.get("currentAirport")).isEqualTo(CNH_CODE);
        assertThat(jsonFlightEvent.get("planeId")).isEqualTo(PLANE_ID);
        String exceptionMessage = new String(record.headers().lastHeader("x-exception-message").value());
        assertThat(exceptionMessage).contains("NoFlightFoundException");
    }

}
