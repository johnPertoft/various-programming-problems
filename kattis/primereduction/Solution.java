import java.io.*;
import java.util.*;
import java.util.function.*;

public class Solution {
  
  private static boolean[] primeSieve(int primeLimit) {
    boolean[] sieve = new boolean[primeLimit];
    Arrays.fill(sieve, true);
    sieve[0] = false;
    sieve[1] = false;

    // Multiples of two are not prime
    for (int p = 4; p < primeLimit; p += 2) {
      sieve[p] = false;
    }

    for (int p = 3; p < primeLimit; p += 2) {
      if (!sieve[p]) continue;
      
      // Multiples of prime p are not prime
      for (int q = p + p; q < primeLimit; q += p) {
        sieve[q] = false;
      }
    }

    return sieve;
  }

  // TODO?
  private static int[] smallestPrimeFactor(boolean[] sieve) {
    return null;
  }

  private static LinkedList<Integer> primeFactors(
      boolean[] sieve, 
      int primeLimit, 
      ArrayList<Integer> primes,
      int x) {
    
    LinkedList<Integer> factors = new LinkedList<>();
    
    // Quick exit if we know this is prime from sieve alone
    if (x < primeLimit && sieve[x]) {
      factors.add(x);
      return factors;
    }
    
    // First factor out all 2's
    while (x % 2 == 0) {
      x = x / 2;
      factors.add(2);
    }

    // Then factor out all primes < sqrt(x)
    for (int p : primes) {
      while (x % p == 0) {
        x = x / p;
        factors.add(p);
      }
      if (x == 1) break;
    }

    if (x > 2) {
      factors.add(x);
    }
    
    return factors;
  }

  private static void primeReduction(
      boolean[] sieve,
      int primeLimit,
      ArrayList<Integer> primes,
      int x,
      int iter) {

    final LinkedList<Integer> factors = 
      primeFactors(sieve, primeLimit, primes, x);

    if (factors.size() == 1) {
      System.out.println(x + " " + iter);
    
    } else {
      final int nextX = factors.stream().reduce(0, (a, p) -> a + p);
      primeReduction(sieve, primeLimit, primes, nextX, iter + 1);
    }
  }

  public static void main(String[] args) {
    final BufferedReader in = 
      new BufferedReader(new InputStreamReader(System.in));
   
    final int LIMIT = 1000000000;
    final int primeLimit = 1 + (int) Math.sqrt(LIMIT);
    
    final boolean[] sieve = primeSieve(primeLimit);
    
    // TODO: should find the smallest prime factors for each number
    // and use that instead for quicker factorization
    final ArrayList<Integer> primes = new ArrayList<>(primeLimit);
    for (int p = 3; p < primeLimit; p += 2) {
      if (sieve[p]) primes.add(p);
    }

    final Consumer<String> primeReducer = (String l) -> {
      int x = Integer.parseInt(l);
      if (x != 4) primeReduction(sieve, primeLimit, primes, x, 1);
    };

    in.lines().forEach(primeReducer);
  }
}
