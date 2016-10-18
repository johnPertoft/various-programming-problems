import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import java.awt.Point;

public class BruteForce {

  private static final Point[] DIRECTIONS = {
    new Point(-1, 0), new Point(0, -1), new Point(1, 0), new Point(0, 1),
    new Point(-1, -1), new Point(-1, 1), new Point(1, -1), new Point(1, 1)
  };

  private static void printRoom(int H, int W, Set<Point> zombiePositions) {
    boolean[][] zombie = new boolean[H][W];
    zombiePositions.stream()
      .forEach(p -> zombie[p.y][p.x] = true);
    for (int h = 0; h < H; h++) {
      for (int w = 0; w < W; w++) {
        System.out.print(zombie[h][w] ? "X" : ".");
      }
      System.out.println();
    }
  }
  
  /**
   * Returns the number of zombie kills from a shot taken at the given
   * position and with the given direction.
   */
  private static int fire(Point pos, Point dir, 
      int H, int W, Set<Point> zombiePositions) {

    Set<Point> affectedPositions = new HashSet<>();

    Point currentPos = new Point(pos);
    while (currentPos.y >= 0 && currentPos.y < H && 
        currentPos.x >= 0 && currentPos.x < W) {
      
      affectedPositions.add(new Point(currentPos));
      
      // Add all neighboring positions (grazing hits)
      for (Point d : DIRECTIONS) {
        affectedPositions.add(
            new Point(currentPos.x + d.x, currentPos.y + d.y));
      }

      // Stop if we have hit a zombie
      if (zombiePositions.contains(currentPos)) break;
      
      currentPos.y += dir.y;
      currentPos.x += dir.x;
    }
    
    // Only keep intersection
    affectedPositions.retainAll(zombiePositions);

    return affectedPositions.size();
  }

  private static int solve(int H, int W, Set<Point> zombiePositions) {
    // Ok with parallel?
    Optional<Integer> mostKills = IntStream.range(0, H)
      .mapToObj(h -> IntStream.range(0, W).mapToObj(w -> new Point(w, h)))
      .flatMap(Function.identity())
      .filter(p -> !zombiePositions.contains(p))
      .parallel()
      .flatMap(p -> Arrays.stream(DIRECTIONS)
          .map(d -> fire(p, d, H, W, zombiePositions))) 
      .max((a, b) -> a.compareTo(b));

    return mostKills.orElse(0);
  }

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    final int H = in.nextInt();
    final int W = in.nextInt();
    Set<Point> zombiePositions = new HashSet<>();
    while (in.hasNext()) {
      int h = in.nextInt();
      int w = in.nextInt();
      zombiePositions.add(new Point(w, h));
    }

    //printRoom(H, W, zombiePositions);
    System.out.println(solve(H, W, zombiePositions));
  }
}
