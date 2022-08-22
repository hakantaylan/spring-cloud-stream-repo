package bertsanders.demo.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.support.MessageBuilder;

@Slf4j
@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class ApplicationTests {

  @Autowired
  private InputDestination inputDestination;
  @Autowired
  private OutputDestination outputDestination;

  @Test
  void testRandomMessages() {

    Random randomizer = new Random();
    IntStream.range(1, 20)
        .map(i -> randomizer.nextInt(1000) + 1)
        .forEach(this::performTest);
  }

  @SneakyThrows
  private void performTest(int messageInput) {
    Message<Integer> message = new GenericMessage<>(messageInput);
    inputDestination.send(message, "input-topic");

    Message<byte[]> output = outputDestination.receive(1_000, "output-topic");
    assertThat(output).as("Failed to receive a message").isNotNull();

    BigInteger primeNumber = new ObjectMapper().readValue(output.getPayload(), BigInteger.class);

    assertThat(primeNumber.isProbablePrime(10)).isTrue();
  }

  @Test
  public void testUpperCaseTopic()  {
    Message<String> message = new GenericMessage<>("Deneme");
    inputDestination.send(message, "uppercase-input-topic");
    Message<byte[]> output = outputDestination.receive(1_000, "uppercase-output-topic");
    String val = new String(output.getPayload(), StandardCharsets.UTF_8);

    assertThat(val).isEqualTo("DENEME");
  }

  @Test
  public void sampleTest() {
    try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
            TestChannelBinderConfiguration.getCompleteConfiguration(SampleFunctionConfiguration.class))
            .run("--spring.cloud.function.definition=uppercase")) {
      InputDestination source = context.getBean(InputDestination.class);
      OutputDestination target = context.getBean(OutputDestination.class);
      source.send(new GenericMessage<byte[]>("hello".getBytes()));
      assertThat(target.receive().getPayload()).isEqualTo("HELLO".getBytes());
    }
  }

  @Test
  public void testMultipleFunctions() {
    try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
            TestChannelBinderConfiguration.getCompleteConfiguration(SampleFunctionConfiguration.class))
            .run("--spring.cloud.function.definition=uppercase;reverse")) {

      InputDestination inputDestination = context.getBean(InputDestination.class);
      OutputDestination outputDestination = context.getBean(OutputDestination.class);

      Message<byte[]> inputMessage = MessageBuilder.withPayload("Hello".getBytes()).build();
      inputDestination.send(inputMessage, "uppercase-input-topic");
      inputDestination.send(inputMessage, "reverse-input-topic");

      Message<byte[]> outputMessage = outputDestination.receive(0, "uppercase-output-topic");
      assertThat(outputMessage.getPayload()).isEqualTo("HELLO".getBytes());

      outputMessage = outputDestination.receive(0, "reverse-output-topic");
      assertThat(outputMessage.getPayload()).isEqualTo("olleH".getBytes());
    }
  }

}
