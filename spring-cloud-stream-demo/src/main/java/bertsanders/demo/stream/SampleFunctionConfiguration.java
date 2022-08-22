package bertsanders.demo.stream;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@EnableAutoConfiguration
@Configuration
public class SampleFunctionConfiguration {

    @Bean
    public Function<String, String> uppercase() {
        return String::toUpperCase;
    }

    @Bean
    public Function<String, String> reverse() {
        return value -> new StringBuilder(value).reverse().toString();
    }
}
