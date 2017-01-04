import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Still basic backtracking but trying to visit cells in a better order
 * by finding the cell with few options
 */

public class Solution2 {

  private static final int EMPTY = 0;
  private static final int MORE_THAN_ONE_SOLUTION = -1;

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

  private static class Cell {
    public int row;
    public int col;
    public Cell next;
    public Cell prev;
    public int numOptions;
    public int[] options = new int[9];
    
    public Cell(int row, int col) {
      this.row = row;
      this.col = col;
    }

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
  
  private static void computeOptions(int[][] board, Cell cell) {
    boolean[] taken = new boolean[10];
    
    // Row
    for (int col = 0; col < 9; col++) 
      taken[board[cell.row][col]] = true;

    // Col
    for (int row = 0; row < 9; row++)
      taken[board[row][cell.col]] = true;

    // Box
    int row = (cell.row / 3) * 3;
    int col = (cell.col / 3) * 3;
    for (int r = 0; r < 3; r++) {
      for (int c = 0; c < 3; c++) {
        taken[board[row+r][col+c]] = true;
      }
    }
  
    cell.numOptions = 0;
    for (int num = 1; num <= 9; num++) {
      if (!taken[num]) 
        cell.options[cell.numOptions++] = num;
    }
  }

  private static void solve(
      int[][] board, 
      Cell head,
      AtomicInteger numSolutions,
      AtomicReference<String> solvedBoard) {
    
    /*
    // "Quick" exit of recursion stack.
    if (numSolutions.get() == MORE_THAN_ONE_SOLUTION) {
      return;
    }
    */
   
    // Possible bug? Will never backtrack to find other solutions for 
    // cells that were given

    if (head.next == null) {
      // Base case
      computeOptions(board, head);
      
      if (head.numOptions == 0) return;

      board[head.row][head.col] = head.options[0];
      solvedBoard.set(boardToString(board));
      board[head.row][head.col] = EMPTY;
      if (numSolutions.incrementAndGet() > 1) {
        // Used to signal that we want to stop the backtracking.
        numSolutions.set(MORE_THAN_ONE_SOLUTION);
      }

    } else {
      // Compute options for all remaining cells and choose the one
      // with the fewest options.
      computeOptions(board, head);
      Cell fewestOptionsCell = head;
      Cell c = head.next;
      while (c != null) {
        computeOptions(board, c);
        if (c.numOptions < fewestOptionsCell.numOptions) {
          fewestOptionsCell = c;
        }
        c = c.next;
      }

      // Return if we have reached an inconsistency
      if (fewestOptionsCell.numOptions == 0) return;
     
      // Remove the cell with fewest options from remaining cells
      fewestOptionsCell.removeSelf();
      Cell nextHead = fewestOptionsCell == head ? head.next : head; 
      
      // Try the possible options for the cell with fewest options
      for (int i = 0; i < fewestOptionsCell.numOptions; i++) {
        board[fewestOptionsCell.row][fewestOptionsCell.col] = 
          fewestOptionsCell.options[i];
        solve(board, nextHead, numSolutions, solvedBoard);
        
        // "Quick" exit of recursion stack.
        if (numSolutions.get() == MORE_THAN_ONE_SOLUTION) {
          return;
        }
      }

      // Add the cell back to remaining cells when all options are tried
      fewestOptionsCell.addSelf();
      board[fewestOptionsCell.row][fewestOptionsCell.col] = EMPTY;
    }
  }

  public static String solve(int[][] board) {
    if (!checkViolations(board)) {
      return "Find another job";
    } else {
    
      // Create the list of remaining cells
      Cell head = null;
      for (int row = 0; row < 9; row++) {
        for (int col = 0; col < 9; col++) {
          if (board[row][col] == EMPTY) {
            if (head == null) head = new Cell(row, col);
            else {
              Cell c = new Cell(row, col);
              head.prev = c;
              c.next = head;
              head = c;
            }
          }
        }
      }

      // If it was already solved
      if (head == null) return boardToString(board);

      // Just using these as references to strings and integers 
      // instead of global values.
      AtomicReference<String> solvedBoard = new AtomicReference<>("");
      AtomicInteger numSolutions = new AtomicInteger(0);
      solve(board, head, numSolutions, solvedBoard);
      
      if (numSolutions.get() == 1) return solvedBoard.get();
      else return "Non-unique";
    }
  }
  
  public static void main(String[] args) {
    final Scanner scanner = new Scanner(System.in);
    System.out.println(solve(readBoard(scanner)));
    while (scanner.hasNext()) {
      scanner.nextLine(); // Empty line
      System.out.println("\n" + solve(readBoard(scanner)));
    }

    // Optimisation ideas:
    // - another idea is to have like a 3d structure of options so that
    // if we put down a 3 in some column, we remove that as option from the
    // affected cells. Maybe use some cool bitshifting for this
    // - Faster way to check if we can put a specific number at a given col/row/box
  }
}
