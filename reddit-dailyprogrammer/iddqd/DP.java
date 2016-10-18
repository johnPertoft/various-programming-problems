import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import java.awt.Point;

public class DP {

  /*
   * In this solution it is assumed that the beam fills the cell it is fired
   * from and then fills all cells until and including the zombie that stops it.
   * 
   * Then, all cells that are filled can cause grazing kills. Thus the beam can
   * effectively kill zombies one cell behind the direction it is fired in.
   *
   * This is explained in the lore by the BFG firing have an explosive effect
   * and that the space maring firing it has a protective suit. Totally serious.
   *
   * The solution is then based on basic dynamic programming by using the
   * computed number of zombie kills from the previous cell and adding the extra
   * kills yielded by firing from the next positions instead. There are four
   * cases of this, horizontal, vertical, diagonal, and antidiagonal shots that
   * need to be accounted for.
   *
   * O(n^2) complexity so does not work for the big input.
   */

  private static final int ZOMBIE = -1;
 
  private static int maxHorizontal(int H, int W, Set<Point> zombiePositions) {
    int max = 0;

    for (int h = 0; h < H; h++) {
      int prev;
      
      // Base case,  w = 0
      if (zombiePositions.contains(new Point(0, h))) {
        prev = ZOMBIE;

      } else {
        int kills = 0;
        if (zombiePositions.contains(new Point(0, h-1))) kills++;
        if (zombiePositions.contains(new Point(1, h-1))) kills++;
        if (zombiePositions.contains(new Point(1, h))) kills++;
        if (zombiePositions.contains(new Point(1, h+1))) kills++;
        if (zombiePositions.contains(new Point(0, h+1))) kills++;
        prev = kills;
        if (prev > max) max = prev;
      }
      
      // DP case, w > 0
      for (int w = 1; w < W; w++) {
        if (zombiePositions.contains(new Point(w, h))) {
          prev = ZOMBIE;
        
        } else {
          int furtherKills = 0;
          if (zombiePositions.contains(new Point(w+1, h-1))) furtherKills++;
          if (zombiePositions.contains(new Point(w+1, h))) furtherKills++;
          if (zombiePositions.contains(new Point(w+1, h+1))) furtherKills++;
          
          if (prev != ZOMBIE) {
            prev = prev + furtherKills;
            if (prev > max) max = prev;
          } else {
            int previousKills = 1;
            if (zombiePositions.contains(new Point(w, h-1))) previousKills++;
            if (zombiePositions.contains(new Point(w-1, h-1))) previousKills++;
            if (zombiePositions.contains(new Point(w-2, h-1))) previousKills++;
            if (zombiePositions.contains(new Point(w-2, h))) previousKills++;
            if (zombiePositions.contains(new Point(w-2, h+1))) previousKills++;
            if (zombiePositions.contains(new Point(w-1, h+1))) previousKills++;
            if (zombiePositions.contains(new Point(w, h+1))) previousKills++;
            prev = previousKills + furtherKills; 
            if (prev > max) max = prev;
          }
        }
      }
    }
    
    return max;
  }
  
  private static int maxVertical(int H, int W, Set<Point> zombiePositions) {
    int max = 0;

    for (int w = 0; w < W; w++) {
      int prev;

      // Base case, h = 0
      if (zombiePositions.contains(new Point(w, 0))) {
        prev = ZOMBIE;

      } else {
        int kills = 0;
        if (zombiePositions.contains(new Point(w-1, 0))) kills++;
        if (zombiePositions.contains(new Point(w-1, 1))) kills++;
        if (zombiePositions.contains(new Point(w, 1))) kills++;
        if (zombiePositions.contains(new Point(w+1, 1))) kills++;
        if (zombiePositions.contains(new Point(w+1, 0))) kills++;
        prev = kills;
        if (prev > max) max = prev;
      }

      // DP case, h > 0
      for (int h = 1; h < H; h++) {
        if (zombiePositions.contains(new Point(w, h))) {
          prev = ZOMBIE;

        } else {
          int furtherKills = 0;
          if (zombiePositions.contains(new Point(w-1, h+1))) furtherKills++;
          if (zombiePositions.contains(new Point(w, h+1))) furtherKills++;
          if (zombiePositions.contains(new Point(w+1, h+1))) furtherKills++;

          if (prev != ZOMBIE) {
            prev = prev + furtherKills;
            if (prev > max) max = prev;
          
          } else {
            int previousKills = 1;
            if (zombiePositions.contains(new Point(w+1, h))) previousKills++;
            if (zombiePositions.contains(new Point(w+1, h-1))) previousKills++;
            if (zombiePositions.contains(new Point(w+1, h-2))) previousKills++;
            if (zombiePositions.contains(new Point(w, h-2))) previousKills++;
            if (zombiePositions.contains(new Point(w-1, h-2))) previousKills++;
            if (zombiePositions.contains(new Point(w-1, h-1))) previousKills++;
            if (zombiePositions.contains(new Point(w-1, h))) previousKills++;
            prev = previousKills + furtherKills;
            if (prev > max) max = prev;
          }
        }
      }
    }

    return max;
  }
  
  // Returns the next dp value for a diagonal
  private static int diagonalDPCase(
      int prev,
      int h, int w,
      int H, int W, 
      Set<Point> zombiePositions) {
        
    if (zombiePositions.contains(new Point(w, h))) {
      return ZOMBIE;
    
    } else {
      int furtherKills = 0;
      if (zombiePositions.contains(new Point(w+1, h-1))) furtherKills++;
      if (zombiePositions.contains(new Point(w+1, h))) furtherKills++;
      if (zombiePositions.contains(new Point(w+1, h+1))) furtherKills++;
      if (zombiePositions.contains(new Point(w, h+1))) furtherKills++;
      if (zombiePositions.contains(new Point(w-1, h+1))) furtherKills++;

      if (prev != ZOMBIE) {
        return prev + furtherKills;
      
      } else {
        int previousKills = 1;
        if (zombiePositions.contains(new Point(w, h-1))) previousKills++;
        if (zombiePositions.contains(new Point(w, h-2))) previousKills++;
        if (zombiePositions.contains(new Point(w-1, h-2))) previousKills++;
        if (zombiePositions.contains(new Point(w-2, h-2))) previousKills++;
        if (zombiePositions.contains(new Point(w-2, h-1))) previousKills++;
        if (zombiePositions.contains(new Point(w-2, h))) previousKills++;
        if (zombiePositions.contains(new Point(w-1, h))) previousKills++;
        return previousKills + furtherKills;
      }
    }
  }
  
  private static int maxDiagonal(int H, int W, Set<Point> zombiePositions) {
    int max = 0;
  
    // First go through upper diagonals
    for (int startCol = 0; startCol < W; startCol++) {
      int prev;
      
      // Base case, h = 0
      if (zombiePositions.contains(new Point(startCol, 0))) {
        prev = ZOMBIE;
      
      } else {
        int w = startCol;
        int kills = 0;
        if (zombiePositions.contains(new Point(w+1, 0))) kills++;
        if (zombiePositions.contains(new Point(w+1, 1))) kills++;
        if (zombiePositions.contains(new Point(w, 1))) kills++;
        if (zombiePositions.contains(new Point(w-1, 1))) kills++;
        if (zombiePositions.contains(new Point(w-1, 0))) kills++;
        prev = kills;
        if (prev > max) max = prev;
      }

      // DP case, h > 0
      int diagLen = Math.min(W - startCol, H);
      for (int h = 1; h < diagLen; h++) {
        int w = startCol + h;
        
        prev = diagonalDPCase(prev, h, w, H, W, zombiePositions);
        if (prev > max) max = prev;
      }
    }

    // Then the lower diagonals
    for (int startRow = 1; startRow < H; startRow++) {
      
      int prev = 0;

      // Base case, w = 0
      if (zombiePositions.contains(new Point(0, startRow))) {
        prev = ZOMBIE;
      
      } else {
        int h = startRow;
        int kills = 0;
        if (zombiePositions.contains(new Point(0, h-1))) kills++;
        if (zombiePositions.contains(new Point(1, h-1))) kills++;
        if (zombiePositions.contains(new Point(1, h))) kills++;
        if (zombiePositions.contains(new Point(1, h+1))) kills++;
        if (zombiePositions.contains(new Point(0, h+1))) kills++;
        prev = kills;
        if (prev > max) max = prev;
      }

      // DP case, w > 0
      int diagLen = Math.min(H - startRow, W);
      for (int w = 1; w < diagLen; w++) {
        int h = startRow + w;
        
        prev = diagonalDPCase(prev, h, w, H, W, zombiePositions);
        if (prev > max) max = prev;
      }
    }

    return max;
  }
  
  private static int antidiagonalDPCase(
      int prev,
      int h, int w,
      int H, int W,
      Set<Point> zombiePositions) {
    
    if (zombiePositions.contains(new Point(w, h))) {
      return ZOMBIE;
    
    } else {
      int furtherKills = 0;
      if (zombiePositions.contains(new Point(w-1, h-1))) furtherKills++;
      if (zombiePositions.contains(new Point(w-1, h))) furtherKills++;
      if (zombiePositions.contains(new Point(w-1, h+1))) furtherKills++;
      if (zombiePositions.contains(new Point(w, h+1))) furtherKills++;
      if (zombiePositions.contains(new Point(w+1, h+1))) furtherKills++;

      if (prev != ZOMBIE) {
        return prev + furtherKills;
      
      } else {
        int previousKills = 1;
        if (zombiePositions.contains(new Point(w, h-1))) previousKills++;
        if (zombiePositions.contains(new Point(w, h-2))) previousKills++;
        if (zombiePositions.contains(new Point(w+1, h-2))) previousKills++;
        if (zombiePositions.contains(new Point(w+2, h-2))) previousKills++;
        if (zombiePositions.contains(new Point(w+2, h-1))) previousKills++;
        if (zombiePositions.contains(new Point(w+2, h))) previousKills++;
        if (zombiePositions.contains(new Point(w+1, h))) previousKills++;
        return previousKills + furtherKills;
      }
    }
  }

  private static int maxAntidiagonal(int H, int W, Set<Point> zombiePositions) {
    int max = 0;
    
    // First the upper antidiagonals
    for (int startCol = 0; startCol < W; startCol++) {
      int prev;

      // Base case, h = 0
      if (zombiePositions.contains(new Point(startCol, 0))) {
        prev = ZOMBIE;
      
      } else {
        int w = startCol;
        int kills = 0;
        if (zombiePositions.contains(new Point(w-1, 0))) kills++;
        if (zombiePositions.contains(new Point(w-1, 1))) kills++;
        if (zombiePositions.contains(new Point(w, 1))) kills++;
        if (zombiePositions.contains(new Point(w+1, 1))) kills++;
        if (zombiePositions.contains(new Point(w+1, 0))) kills++;
        prev = kills;
        if (prev > max) max = prev;
      }

      // DP case, h > 0
      int diagLen = Math.min(startCol+1, H);
      for (int h = 1; h < diagLen; h++) {
        int w = startCol - h;
        
        prev = antidiagonalDPCase(prev, h, w, H, W, zombiePositions);
        if (prev > max) max = prev;
      }
    }

    // Then the lower antidiagonals
    for (int startRow = 1; startRow < H; startRow++) {
      
      int prev;

      // Base case, w = W-1
      if (zombiePositions.contains(new Point(W-1, startRow))) {
        prev = ZOMBIE;
      
      } else {
        int w = W - 1;
        int h = startRow;
        int kills = 0;
        if (zombiePositions.contains(new Point(w, h-1))) kills++;
        if (zombiePositions.contains(new Point(w-1, h-1))) kills++;
        if (zombiePositions.contains(new Point(w-1, h))) kills++;
        if (zombiePositions.contains(new Point(w-1, h+1))) kills++;
        if (zombiePositions.contains(new Point(w, h+1))) kills++;
        prev = kills;
        if (prev > max) max = prev;
      }

      // DP case, w < W-1
      int diagLen = Math.min(H - startRow, W);
      int ri = 1; // TODO: dont use
      for (int w = W-2; w >= 0; w--) {
        int h = startRow + ri++;
        
        prev = antidiagonalDPCase(prev, h, w, H, W, zombiePositions);
        if (prev > max) max = prev;
      }
    }

    return max;
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
    
    long start = System.currentTimeMillis(); 
    
    int mostKills = Math.max(
        Math.max(
            maxHorizontal(H, W, zombiePositions),
            maxVertical(H, W, zombiePositions)),
        Math.max(
            maxDiagonal(H, W, zombiePositions),
            maxAntidiagonal(H, W, zombiePositions)));

    long end = System.currentTimeMillis();

    System.out.println("Most possible kills: " + mostKills);
    System.out.println("Elapsed time: " + (end - start));
  }
}
