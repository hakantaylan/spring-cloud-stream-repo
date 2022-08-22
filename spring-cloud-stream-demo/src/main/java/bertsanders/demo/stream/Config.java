package bertsanders.demo.stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.util.function.Function;

@Configuration
public class Config {

    @Bean
    Function<Integer, BigInteger> calculateNthPrime(PrimeNumberService primeNumberService) {
        return primeNumberService::nthPrime;
    }
}
