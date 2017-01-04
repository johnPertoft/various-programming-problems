import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Still basic backtracking but trying to visit cells in a better order
 * by finding the cell with few options. This time by keeping options as
 * a 3d structure for all cells to allow for quick cancelling of options
 * for other cells.
 */

public class Solution3 {

  private static final int EMPTY = 0;
  private static final int MORE_THAN_ONE_SOLUTION = -1;

  private static final int ONE = 1;
  private static final int TWO = ONE << 1;
  private static final int THREE = ONE << 2;
  private static final int FOUR = ONE << 3;
  private static final int FIVE = ONE << 4;
  private static final int SIX = ONE << 5;
  private static final int SEVEN = ONE << 6;
  private static final int EIGHT = ONE << 7;
  private static final int NINE = ONE << 8;

  private static final int[] NUM_REPRESENTATIONS = new int[]{-1, // easier indexing
    ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE};

  private static int[][] readBoard(Scanner scanner) {
    int[][] board = new int[9][9];
    for (int r = 0; r < 9; r++) {
      String[] s = scanner.nextLine().split(" ");
      for (int c = 0; c < 9; c++) {
        board[r][c] = Integer.parseInt(s[c]);
      }
    }

    return board;
  }

  private static String boardToString(int[][] board) {
    StringBuilder sb = new StringBuilder();
    String newRow = "";
    for (int row = 0; row < 9; row++) {
      sb.append(newRow);
      newRow = "\n";
      for (int col = 0; col < 8; col++) {
        sb.append(board[row][col] + " ");
      }
      sb.append(board[row][8]);
    }

    return sb.toString();
  }

  // TODO: pretty sure this can be done quicker
  private static boolean checkViolations(int[][] board) {
    // Rows
    for (int row = 0; row < 9; row++) {
      int[] counts = new int[10];
      for (int col = 0; col < 9; col++) {
        if (board[row][col] != EMPTY && ++counts[board[row][col]] > 1) {
          return false;
        }
      }
    }

    // Cols
    for (int col = 0; col < 9; col++) {
      int[] counts = new int[10];
      for (int row = 0; row < 9; row++) {
        if (board[row][col] != EMPTY && ++counts[board[row][col]] > 1) {
          return false;
        }
      }
    }

    // Boxes
    for (int startRow : new int[]{0, 3, 6}) {
      for (int startCol : new int[]{0, 3, 6}) {
        int[] counts = new int[10];
        for (int r = 0; r < 3; r++) {
          for (int c = 0; c < 3; c++) {
            if (board[startRow+r][startCol+c] != EMPTY 
                && ++counts[board[startRow+r][startCol+c]] > 1) {
              return false;
            }
          }
        }
      }
    }

    return true;
  }

  private static int countBits(int n ) {
    return 
      (n & ONE) +
      ((n & TWO) >> 1) + 
      ((n & THREE) >> 2) +
      ((n & FOUR) >> 3) +
      ((n & FIVE) >> 4) +
      ((n & SIX) >> 5) +
      ((n & SEVEN) >> 6) +
      ((n & EIGHT) >> 7) +
      ((n & NINE) >> 8);
  }

  private static class Cell implements Comparable<Cell> {
    public int row;
    public int col;
    private int[][] options;
    
    public Cell(int row, int col, int[][] options) {
      this.row = row;
      this.col = col;
      this.options = options; // Same for all cells of a sudoku
    }
    
    public int numOptions() {
      // TODO: Should probably also cache which the options are
      return countBits(options[row][col]);
    }
    
    public int compareTo(Cell other) {
      return this.numOptions() - other.numOptions();
    }
  }

  private static void removeOption(int[][] options, int row, int col, int num) {
    int mask = ~NUM_REPRESENTATIONS[num];
    // Row
    for (int c = 0; c < 9; c++) options[row][c] &= mask;

    // Col
    for (int r = 0; r < 9; r++) options[r][col] &= mask;

    // Box
    row = (row / 3) * 3;
    col = (col / 3) * 3;
    for (int r = 0; r < 3; r++) {
      for (int c = 0; c < 3; c++) {
        options[row+r][col+c] &= mask;
      }
    }
  }

  private static void solve(
      int[][] board,
      PriorityQueue<Cell> remainingCells,
      int[][] options, // Bit representing if a number is an option 
      AtomicInteger numSolutions,
      AtomicReference<String> solvedBoard) {
    
    // "Quick" exit of recursion stack.
    if (numSolutions.get() == MORE_THAN_ONE_SOLUTION) return;
  
    if (remainingCells.isEmpty()) {
      // Base case

    } else {
      Cell minCell = remainingCells.peek();
      
      for (int num = 1; num <= 9; num++) {
        if ((options[minCell.row][minCell.col] & NUM_REPRESENTATIONS[num]) > 0) {
          removeOption(options, minCell.row, minCell.col, num);
          // TODO: how to add the option back again?
          // remove num from options in row, col and box 
        }
      }

      // update options[][] then remove it from remainingCells
      // then continue solving
    }
  }

  private static int[][] computeOptions(int[][] board) {
    return null;
  }

  public static String solve(int[][] board) {
    if (!checkViolations(board)) {
      return "Find another job";
    } else {
   
      int[][] options = computeOptions(board);
     
      ArrayList<Cell> cells = new ArrayList<>(100);
      for (int row = 0; row < 9; row++) {
        for (int col = 0; col < 9; col++) {
          if (board[row][col] == EMPTY) 
            cells.add(new Cell(row, col, options));
        }
      }
      
      // If it was already solved
      if (cells.isEmpty()) return boardToString(board);

      PriorityQueue<Cell> remainingCells = new PriorityQueue<>(cells);

      // Just using these as references to strings and integers 
      // instead of global values.
      AtomicReference<String> solvedBoard = new AtomicReference<>("");
      AtomicInteger numSolutions = new AtomicInteger(0);
      
      solve(board, remainingCells, options, numSolutions, solvedBoard);
      
      if (numSolutions.get() == 1) {
        return solvedBoard.get();
      } else {
        return "Non-unique";
      }
    }
  }
  
  public static void main(String[] args) {
    final Scanner scanner = new Scanner(System.in);
    System.out.println(solve(readBoard(scanner)));
    while (scanner.hasNext()) {
      scanner.nextLine(); // Empty line
      System.out.println("\n" + solve(readBoard(scanner)));
    }
    
    // Other ideas
    // - When we have one solution, can we determine whether there are more than one
    // solution by removing things from the solved board?
    //  - Might have to solve this without backtracking? Maybe hard to find other
    // solutions in that case
  }
}
