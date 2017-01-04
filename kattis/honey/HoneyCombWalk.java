import java.util.*;

// Solution with dynamic prorgramming

// Dyn matrix is 3d, 2 for coordinates, 1 for num steps, W[x, y, s]
// the value of a cell is the number of walks of s steps that starts 
// in coord x, y and ends up in the target (origin)
//
// hexagonal coordinate system
// the y coordinate only increases up
// the x coordinate only increases diagonally up
//   _   _
//  / \_/ \
//  \_/ \_/
//  / \_/
//  \_/
// 
// top left is (-1, 1)
// bottom left is (-1, 0) 
// center is (0,0)
// right is (1,0)
//
// recursive step (all cells around the given cell at one fewer steps)
// W[x][y][s] = 
// W[x+1][y][s-1] + 
// W[x+1][y-1][s-1] +
// W[x][y-1][s-1] +
// W[x-1][y][s-1] + 
// W[x-1][y+1][s-1] + 
// W[x][y+1][s-1]
//
// Solution for number of walks of length s is found in W[o][o][s]
// where o is the origin coordinate

public class HoneyCombWalk {
  public static void main(String[] args) {
    int maxSteps = 15;
    int sz = 2 * maxSteps + 1;
    int o = sz / 2;
    
    // precalculate the dyn matrix
    int[][][] W = new int[sz][sz][maxSteps];
    
    // base case (java arrays initialise to 0)
    W[o][o][0] = 1;

    int[] dx = {1, 1, 0, -1, -1, 0};
    int[] dy = {0, -1, -1, 0, 1, 1};

    for (int s = 1; s < maxSteps; s++) {
      for (int x = 0; x < sz; x++) {
        for (int y = 0; y < sz; y++) {
          // use the recursive step but check bounds
          int sumSteps = 0;

          for (int di = 0; di < dx.length; di++) {
            int xi = x + dx[di];
            int yi = y + dy[di];
            if (xi >= 0 && xi < sz && yi >= 0 && yi < sz)
              sumSteps += W[xi][yi][s-1];
          }

          W[x][y][s] = sumSteps;
        }
      }
    }
    
    Scanner scanner = new Scanner(System.in);
    int numTests = scanner.nextInt();

    for (int i = 0; i < numTests; i++) {
      int n = scanner.nextInt();
      System.out.println(W[o][o][n]);
    }
  }
}
