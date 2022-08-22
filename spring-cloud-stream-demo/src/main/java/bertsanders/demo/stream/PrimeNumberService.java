package bertsanders.demo.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Slf4j
@Component
public class PrimeNumberService {

  public BigInteger nthPrime(int input) {
    log.info("Calculating the Nth Prime for " + input);

    int primeCount = 0;
    BigInteger number = BigInteger.ONE;
    while (primeCount < input) {
      number = number.add(BigInteger.ONE);
      if (isPrime(number)) {
        ++primeCount;
      }
    }

    return number;
  }

  private boolean isPrime(BigInteger candidate) {
    if (candidate.compareTo(BigInteger.ONE) <= 0) {
      return false;
    }

    BigInteger potentialDivisor = BigInteger.TWO;
    while (!candidate.mod(potentialDivisor).equals(BigInteger.ZERO)) {
      potentialDivisor = potentialDivisor.add(BigInteger.ONE);
    }

    return potentialDivisor.equals(candidate);
  }
}
