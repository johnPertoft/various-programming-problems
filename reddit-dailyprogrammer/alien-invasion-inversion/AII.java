import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class AII {
  
  private static int min(int... l) {
    int m = Integer.MAX_VALUE;
    for (int i : l) {
      if (i < m) m = i;
    }

    return m;
  }

  public static void main(String[] args) {
    // KEY INFORMATION HERE BRO
    // Note: has to be a square! But maybe make general rectangle solution also?

    Scanner in = new Scanner(System.in);
    int N = in.nextInt();
    int[][] D = new int[N][N];
    int bestD = Integer.MIN_VALUE;
    int bestR = 0;
    int bestC = 0;
    for (int r = 0; r < N; r++) {
      String row = in.next();
      for (int c = 0; c < N; c++) {
        char tile = row.charAt(c);
        if (tile == 'X') 
          D[r][c] = 0;
        else if (r == 0 || c == 0) 
          D[r][c] = 1;
        else {
          D[r][c] = min(r, c, D[r][c-1], D[r-1][c], D[r-1][c-1]) + 1;
          if (D[r][c] > D[bestR][bestC]) {
            bestR = r;
            bestC = c;
          }
        }
      }
    }

    // D[bestR, bestC] defines the length of the side of the square that
    // has its bottom right corner in bestR, bestC
    
    int dropships = D[bestR][bestC] * D[bestR][bestC];
    System.out.println(dropships + " dropships!");
  }
}
