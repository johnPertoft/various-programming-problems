import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class InfinityLoop {
  
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
  
  private static void printGrid(Integer[][] grid) {
    printGrid(grid, null, -1, -1);
  }

  private static void printGrid(Integer[][] grid, boolean[][] locked) {
    printGrid(grid, locked, -1, -1);
  }

  private static void printGrid(
      Integer[][] grid, 
      boolean[][] locked, 
      int H, 
      int W) {
    
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

    if (H != -1 && W != -1) {
      System.out.println("Current cell " + visualized[grid[H][W]]);
    }
    
    for (int h = 0; h < grid.length; h++) {
      for (int w = 0; w < grid[0].length; w++) {
        if (locked != null) {
          if (H == h && W == w) {
            System.out.print('O');
          } else if (locked[h][w])
            System.out.print(visualized[grid[h][w]]);
          else
            System.out.print("#");
        } else {
          System.out.print(visualized[grid[h][w]]);
        }
      }
      System.out.println();
    }
  }

  // Turn all the outer cells to not point outwards. 
  private static boolean preprocess(Integer[][] grid, boolean[][] locked) {
    int H = grid.length;
    int W = grid[0].length;
    
    // Preprocess top and bottom rows of grid.
    for (int w = 0; w < W; w++) {
      for (int h : new int[] {0, H-1}) {
        // TODO: can also do something with like ┓
        switch (grid[h][w]) {
          case VERT:
            grid[h][w] = HORZ; // Rotate and fall through
          case HORZ: // Fall through
          case EMPTY:
            locked[h][w] = true;
            break;
          case CROSS:
            return false; // Not compatible
        }
      }
    }

    // Preprocess left- and rightmost columns of grid.
    for (int h = 0; h < H; h++) {
      for (int w : new int[] {0, W-1}) {
        switch (grid[h][w]) {
          case HORZ:
            grid[h][w] = VERT; // Rotate and fall through
          case VERT: // Fall through
          case EMPTY:
            locked[h][w] = true;
            break;
          case CROSS:
            return false; // Not compatible
        }
      }
    }

    // Preprocess rest of grid.
    for (int h = 1; h < H - 1; h++) {
      for (int w = 1; w < W - 1; w++) {
        if (grid[h][w] == EMPTY || grid[h][w] == CROSS)
          locked[h][w] = true;
      }
    }

    return true;
  }

  private static class Cell {
    Cell(int h, int w) {
      this.h = h;
      this.w = w;
    }
    public final int h;
    public final int w;
    public Cell next;
    public Cell prev;
  
    public int numOptions;
    public int[] options = new int[4];

    public void removeSelf() {
      if (prev == null) next.prev = null;
      else if (next == null) prev.next = null;
      else {
        prev.next = next;
        next.prev = prev;
      }
    }

    public void addSelf() {
      if (prev == null) next.prev = this;
      else if (next == null) prev.next = this;
      else {
        prev.next = this;
        next.prev = this;
      }
    }
  }

  // Sets the possible rotations for a cell given
  // the current state of neighbors.
  private static void computeOptions(
      final Cell cell,
      Integer[][] grid, 
      boolean[][] locked) {
    
    final int H = grid.length;
    final int W = grid[0].length;
    final int h = cell.h;
    final int w = cell.w;
    
    // TODO: need to do something smarter here I think
    // First check what constraints we have for this tile
    // given the current state of neighbors.

    boolean requireUp = h > 0 
      ? (locked[h-1][w] && (grid[h-1][w] & DOWN) > 0) 
      : false;
   
    boolean requireDown = h < H - 1
      ? (locked[h+1][w] && (grid[h+1][w] & UP) > 0)
      : false;
   
    boolean requireLeft = w > 0
      ? (locked[h][w-1] && (grid[h][w-1] & RIGHT) > 0)
      : false;

    boolean requireRight = w < W - 1
      ? (locked[h][w+1] && (grid[h][w+1] & LEFT) > 0) 
      : false;

    boolean upWorks = h > 0 
      ? !locked[h-1][w] || requireUp
      : false;

    boolean downWorks = h < H - 1
      ? !locked[h+1][w] || requireDown
      : false;

    boolean leftWorks = w > 0
      ? !locked[h][w-1] || requireLeft
      : false;

    boolean rightWorks = w < W - 1
      ? !locked[h][w+1] || requireRight
      : false;
   
    // Then check which rotations meet these constraints.
    final int o = grid[h][w];
    final int maxRots = (o == HORZ || o == VERT) ? 2 : 4;
    cell.numOptions = 0;
    for (int i = 0; i < maxRots; i++) {
      int r = ROTATIONS[o][i];
      boolean ok = true;

      if ((requireUp && (r & UP) == 0) ||
          (requireDown && (r & DOWN) == 0) ||
          (requireLeft && (r & LEFT) == 0) ||
          (requireRight && (r & RIGHT) == 0) ||
          (!upWorks && (r & UP) > 0) ||
          (!downWorks && (r & DOWN) > 0) ||
          (!leftWorks && (r & LEFT) > 0) ||
          (!rightWorks && (r & RIGHT) > 0)) {
        
        ok = false;
      }

      if (ok) cell.options[cell.numOptions++] = r;
    }
  }
  
  static int numSolutions = 0;
  private static void next(
      final Cell head,
      Integer[][] grid, 
      boolean[][] locked) {
    
    final int H = grid.length;
    final int W = grid[0].length;

    if (head.next == null) {
      // Base case, only one cell left.
      computeOptions(head, grid, locked);
      if (head.numOptions == 0) return;
      
      for (int i = 0; i < head.numOptions; i++) {
        grid[head.h][head.w] = head.options[i];
        numSolutions++;
      }
      
    } else {
      // Find the cell with fewest options.
      Cell minCell = head;
      computeOptions(minCell, grid, locked);
      Cell c = minCell.next;
      while (true) {
        computeOptions(c, grid, locked);
        
        if (c.numOptions < minCell.numOptions)
          minCell = c;

        if (c.next == null) break;
        else c = c.next;
      }

      if (minCell.numOptions == 0) return;
     
      // Then remove the cell with fewest options from the list.
      minCell.removeSelf();
      locked[minCell.h][minCell.w] = true;
      Cell nextHead = minCell == head ? minCell.next : head;
      
      // Try all the possible options.
      for (int i = 0; i < minCell.numOptions; i++) {
        grid[minCell.h][minCell.w] = minCell.options[i];
        next(nextHead, grid, locked); 
      }

      // Unlock and add it back and find more solutions.
      locked[minCell.h][minCell.w] = false;
      minCell.addSelf();
    }
  }

  private static boolean solve(Integer[][] grid, boolean[][] locked) {
    final int H = grid.length;
    final int W = grid[0].length;
    
    // TODO: might be better to start with outer perimeter ordered first
    Cell head = IntStream.range(0, H)
      .mapToObj(h -> IntStream.range(0, W).mapToObj(w -> new Cell(h, w)))
      .flatMap(Function.identity())
      .filter(c -> !locked[c.h][c.w])
      .reduce((c1, c2) -> {
        // Next here points to the "left" in the stream
        // (but it's kind of arbitrary anyway)
        c1.prev = c2;
        c2.next = c1;
        return c2;
      }).get();
  
    next(head, grid, locked); 
      
    return false;
  }
  
  public static void main(String[] args) {
    Integer[][] grid = readGrid(System.in);
    boolean[][] locked = new boolean[grid.length][grid[0].length];
   
    printGrid(grid);
    boolean preprocessOk = preprocess(grid, locked);
    if (preprocessOk) {
      System.out.println("Finding solutions");
      solve(grid, locked);
      printGrid(grid);
      System.out.println("Solutions: " + numSolutions);
    }
  }
}
