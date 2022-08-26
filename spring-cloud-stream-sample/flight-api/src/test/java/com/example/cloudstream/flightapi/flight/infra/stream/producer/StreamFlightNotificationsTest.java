package com.example.cloudstream.flightapi.flight.infra.stream.producer;

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
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import static com.example.cloudstream.flightapi.TestData.FLIGHT_ID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EmbeddedKafkaWithTopics
class StreamFlightNotificationsTest {

    @Autowired
    private EmbeddedKafkaBroker broker;

    @Autowired
    private StreamFlightNotifications notifications;

    private KafkaTestUtils kafkaTestUtils;

    @BeforeEach
    void prepare() {
        this.kafkaTestUtils = new KafkaTestUtils(broker);
    }

    @Test
    void flightArrived() throws JSONException {
        Consumer<String, String> consumer = kafkaTestUtils.createConsumer("flight-finished-v1");

        notifications.flightFinished(FLIGHT_ID);

        ConsumerRecord<String, String> record = kafkaTestUtils.getLastRecord(consumer, "flight-finished-v1");

        var jsonFlightEvent = new JSONObject(record.value());
        assertThat(jsonFlightEvent.get("flightId")).isEqualTo(FLIGHT_ID);
    }
}