import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Solution {

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

  private static boolean checkRow(int[][] board, int row, int num) {
    for (int col = 0; col < 9; col++) {
      if (board[row][col] == num) return false;
    }
    
    return true;
  }
  
  private static boolean checkCol(int[][] board, int col, int num) {
    for (int row = 0; row < 9; row++) {
      if (board[row][col] == num) return false;
    }

    return true;
  }
  
  private static boolean checkBox(int[][] board, int row, int col, int num) {
    int startRow = (row / 3) * 3;
    int startCol = (col / 3) * 3;
    for (int r = 0; r < 3; r++) {
      for (int c = 0; c < 3; c++) {
        if (board[startRow+r][startCol+c] == num) return false;
      }
    }

    return true;
  }

  private static void solve(
      int[][] board, 
      int row, 
      int col, 
      AtomicInteger solutions,
      AtomicReference<String> solvedBoard) {
    
    // "Quick" exit of recursion stack.
    if (solutions.get() == MORE_THAN_ONE_SOLUTION) {
      return;
    }

    // Base case, all cells visited.
    if (row > 8) {
      // If here, we have found a solution
      final int numSolutions = solutions.incrementAndGet();
      
      solvedBoard.set(boardToString(board));

      if (numSolutions > 1) {
        // Used to signal that we want to stop the backtracking.
        solutions.set(MORE_THAN_ONE_SOLUTION); 
      }

      return;
    }
    
    final int nextCol = (col + 1) % 9;
    final int nextRow = nextCol == 0 ? row + 1 : row;

    if (board[row][col] != EMPTY) {
      solve(board, nextRow, nextCol, solutions, solvedBoard);
    } else {
      // Just naively try all possibilities at the moment
      for (int num = 1; num <= 9; num++) {
        if (checkRow(board, row, num) 
            && checkCol(board, col, num) 
            && checkBox(board, row, col, num)) {
          
          board[row][col] = num;
          solve(board, nextRow, nextCol, solutions, solvedBoard);
        }
      }

      // Reset cell on the way back up.
      board[row][col] = EMPTY;
    }
  }

  public static String solve(int[][] board) {
    // TODO: Is it enough to check here? Or can we have a board that looks
    // ok initially but cant actually be solved?
    if (!checkViolations(board)) {
      return "Find another job";
    } else {
      // Just using these as references to strings and integers 
      // instead of global values.
      AtomicReference<String> solvedBoard = new AtomicReference<>("");
      AtomicInteger numSolutions = new AtomicInteger(0);
      solve(board, 0, 0, numSolutions, solvedBoard);

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

    // Optimisation ideas:
    // - keep list of options for each cells somehow?
    // - when trying to find another solution, try to backtrack as little
    // as possible by choosing to continue from cells ordered by increasing options
    // somehow
  }
}
