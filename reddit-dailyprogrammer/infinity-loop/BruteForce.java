import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.stream.*;

public class BruteForce {
  
  private final static int EMPTY = 0;
  private final static int UP = 1;
  private final static int RIGHT = 2;
  private final static int DOWN = 4;
  private final static int LEFT = 8;
  private final static int CROSS = UP + DOWN + RIGHT + LEFT;
  private final static int HORZ = LEFT + RIGHT;
  private final static int VERT = UP + DOWN;
  
  // Precomputed rotations
  private final static int[][] ROTATIONS = {
    {0, 0, 0, 0},
    {1, 2, 4, 8}, 
    {2, 4, 8, 1},
    {3, 6, 12, 9}, 
    {4, 8, 1, 2},
    {5, 10, 5, 10}, 
    {6, 12, 9, 3},
    {7, 14, 13, 11}, 
    {8, 1, 2, 4},
    {9, 3, 6, 12}, 
    {10, 5, 10, 5},
    {11, 7, 14, 13}, 
    {12, 9, 3, 6},
    {13, 11, 7, 14}, 
    {14, 13, 11, 7},
    {15, 15, 15, 15}
  };

  public static Integer[][] readGrid(InputStream inputStream) {
    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
    Integer[][] grid = in.lines()
      .map(s -> Arrays.stream(s.split(" "))
          .map(Integer::parseInt)
          .toArray(Integer[]::new))
      .toArray(Integer[][]::new);
    
    return grid;
  }

  public static String gridToString(Integer[][] grid) {
    final char[] visualized = {
      ' ', // 0,  empty
      '╹', // 1,  up
      '╺', // 2,  right
      '┗', // 3,  up, right
      '╻', // 4,  down
      '┃', // 5,  up, down
      '┏', // 6,  down, right
      '┣', // 7,  up, down, right
      '╸', // 8,  left
      '┛', // 9,  up, left
      '━', // 10, left, right
      '┻', // 11, up, left, right
      '┓', // 12, down, left 
      '┫', // 13, up, down, left
      '┳', // 14, down, left, right
      '╋'  // 15, up, down, left, right
    };
    
    StringBuilder sb = new StringBuilder();
    for (Integer[] line : grid) {
      for (int i : line) {
        sb.append(visualized[i]);
      }
      sb.append("\n");
    }
    
    return sb.toString();
  }
  
  // Apply all rotations specified in the rotation string and return a new
  // grid.
  private static Integer[][] applyRotations(final Integer[][] grid, String rotations) {
    final int H = grid.length;
    final int W = grid[0].length;

    Integer[][] rotatedGrid = new Integer[H][W];
    
    for (int h = 0; h < H; h++) {
      for (int w = 0; w < W; w++) {
        int r = Integer.parseInt(String.valueOf(rotations.charAt(h * W + w)));
        rotatedGrid[h][w] = ROTATIONS[grid[h][w]][r];
      }
    }
    
    return rotatedGrid;
  }
  
  // Check if the pipes in all tiles are connected to something.
  private static boolean allConnected(Integer[][] grid) {
    final int H = grid.length;
    final int W = grid[0].length;
    
    for (int h = 0; h < H; h++) {
      for (int w = 0; w < W; w++) {
        final int r = grid[h][w];
        final boolean tileUp = (r & UP) > 0;
        final boolean tileDown = (r & DOWN) > 0;
        final boolean tileLeft = (r & LEFT) > 0;
        final boolean tileRight = (r & RIGHT) > 0;
        
        // Check the neighbor above
        if (h != 0) {
          boolean neighborDown = (grid[h-1][w] & DOWN) > 0;
          if (neighborDown && !tileUp) return false;
          if (!neighborDown && tileUp) return false;
        }

        // Check the neighbor to the left.
        if (w != 0) {
          boolean neighborRight = (grid[h][w-1] & RIGHT) > 0;
          if (neighborRight && !tileLeft) return false;
          if (!neighborRight && tileLeft) return false;
        }

        // Check the neighbor to the right.
        if (w != W-1) {
          boolean neighborLeft = (grid[h][w+1] & LEFT) > 0;
          if (neighborLeft && !tileRight) return false;
          if (!neighborLeft && tileRight) return false;
        }

        // Check the neighbor below
        if (h != H-1) {
          boolean neighborUp = (grid[h+1][w] & UP) > 0;
          if (neighborUp && !tileDown) return false;
          if (!neighborUp && tileDown) return false;
        }
        
        // If pointing right outside grid.
        if (w == W - 1 && tileRight) return false;
        
        // If pointing left outside grid.
        if (w == 0 && tileLeft) return false;

        // If pointing down outside grid.
        if (h == H - 1 && tileDown) return false;

        // If pointing up outside grid.
        if (h == 0 && tileUp) return false;
      }
    }

    return true;
  }

  /**
   * Brute force solution where we map over all possible 
   * rotation permutations over the tiles and check if
   * it is a solution.
   *
   * Each rotation permutation is represented by a base 4
   * number in which the position in the number represent
   * the cell on which the digit at that position operate 
   * on.
   */
  public static void solve(final Integer[][] grid, int maxSolutions) {
    final int R = 4;
    final int H = grid.length;
    final int W = grid[0].length;
    
    LongStream.range(0, (long) Math.pow(R, W*H)).parallel()
      .mapToObj(i -> String.format("%" + H*W + "s", Long.toString(i, R)).replaceAll(" ", "0"))
      .map(rots -> applyRotations(grid, rots))
      .filter(BruteForce::allConnected)
      .limit(maxSolutions)
      .map(BruteForce::gridToString)
      .forEach(System.out::println);
  }

  public static void main(String[] args) {
    final Integer[][] grid = readGrid(System.in);
    
    final int N = grid.length * grid[0].length;
    if (N > 20) System.out.println("Too big input for this brute force solution");
    else solve(grid, 1);
  }
}
