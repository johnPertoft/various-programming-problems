import java.util.*;
import java.util.stream.*;
import java.util.function.Function;

public class Sparse {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    
    final int N = in.nextInt();
    Map<String, Long> counts = Stream.generate(in::next)
      .limit(N)
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    
    final int Q = in.nextInt();
    Stream.generate(in::next)
      .limit(Q)
      .map(s -> counts.getOrDefault(s, 0l))
      .forEach(System.out::println);
  }
}
