import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class NaiveBacktrack {
  
  // TODO: could precompute all rotations

  private final static int EMPTY = 0;
  private final static int UP = 1;
  private final static int RIGHT = 2;
  private final static int DOWN = 4;
  private final static int LEFT = 8;
  private final static int CROSS = UP + DOWN + RIGHT + LEFT;
  private final static int HORZ = LEFT + RIGHT;
  private final static int VERT = UP + DOWN;

  public static Integer[][] readGrid(InputStream inputStream) {
    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
    Integer[][] grid = in.lines()
      .map(s -> Arrays.stream(s.split(" "))
          .map(Integer::parseInt)
          .toArray(Integer[]::new))
      .toArray(Integer[][]::new);
    
    return grid;
  }

  public static void printGrid(Integer[][] grid) {
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

    for (Integer[] line : grid) {
      for (int i : line) 
        System.out.print(visualized[i]);
      System.out.println();
    }
  }

  // Turn all the outer cells to not point outwards. 
  private static void preprocess(Integer[][] grid) {

  }
  
  private static int rotateRight(int cell, int times) {
    return ((cell << times) | (cell >> 4 - times)) & 0xf;
  }
  
  // Check whether the given cell works with the current settings of cell, that
  // is, check whether it works with cells above and to the left (and corner
  // cases).
  private static boolean connected(Integer[][] grid, int h, int w) {
    final int cell = grid[h][w];

    boolean cellUp = (cell & UP) > 0;
    boolean cellDown = (cell & DOWN) > 0;
    boolean cellLeft = (cell & LEFT) > 0;
    boolean cellRight = (cell & RIGHT) > 0;
   
    // If not on top row
    if (h != 0) {
      boolean neighborDown = (grid[h-1][w] & DOWN) > 0;
      if (neighborDown && !cellUp) return false;
      if (!neighborDown && cellUp) return false;
    }
    
    // If not on leftmost column
    if (w != 0) {
      boolean neighborRight = (grid[h][w-1] & RIGHT) > 0;
      if (neighborRight && !cellLeft) return false;
      if (!neighborRight && cellLeft) return false;
    }
    
    // If pointing right outside grid.
    if (w == grid[0].length - 1 && cellRight) return false;
    
    // If pointing left outside grid.
    if (w == 0 && cellLeft) return false;

    // If pointing down outside grid.
    if (h == grid.length - 1 && cellDown) return false;

    // If pointing up outside grid.
    if (h == 0 && cellUp) return false;

    // All tests passed?
    return true;
  }

  private static boolean solve(Integer[][] g, int h, int w, int H, int W) {
    final int originalCell = g[h][w];

    final int next_w = (w + 1) % W;
    final int next_h = next_w == 0 ? h + 1 : h;

    final int maxRots = originalCell == EMPTY || originalCell == CROSS 
      ? 0 
      : (originalCell == HORZ || originalCell == VERT 
          ? 1 
          : 3);

    if (next_h >= H) {
      // Basecase: We are currently in the last cell, so a solution is found
      // if this last cell can be connected.
      
      for (int rot = 0; rot < maxRots; rot++) {
        // First check if already connected.
        if (connected(g, h, w)) return true;
        
        // Then test to rotate.
        g[h][w] = rotateRight(g[h][w], 1);
      }
      
      // Last chance with "previous" settings.
      return connected(g, h, w);
    }
    
    // Recursive case
    for (int rot = 0; rot < maxRots; rot++) {
      // First check if current settings work at this point.
      if (connected(g, h, w) && solve(g, next_h, next_w, H, W))
        return true;
      
      // Then test to rotate.
      g[h][w] = rotateRight(g[h][w], 1);
    }
  
    // Last chance with "previous" settings.
    return connected(g, h, w) ? solve(g, next_h, next_w, H, W) : false;
  }
  
  public static Optional<Integer[][]> backtrack(Integer[][] grid, int H, int W) {
    
    final long start = System.currentTimeMillis();
    boolean solved = solve(grid, 0, 0, H, W);
    final long end = System.currentTimeMillis();

    if (solved) System.out.println("Found solution in " + (end - start) + "ms");
    else System.out.println("No solution");

    return solved ? Optional.of(grid) : Optional.empty();
  }

  public static void main(String[] args) {
    Integer[][] grid = readGrid(System.in);
    printGrid(grid);
    final int H = grid.length;
    final int W = grid[0].length;
    
    Optional<Integer[][]> maybeSolution = backtrack(grid, H, W);
    maybeSolution.ifPresent(NaiveBacktrack::printGrid);
  }
}
