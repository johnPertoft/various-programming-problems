import java.util.*;

/**
 * The key here compared to previous solution which didnt fit in memory is that we dont have to keep the number of ones as a state in the dp matrix since it can be computed
 */

public class Coke {
  private static final int MAX_COKES = 150;
  private static final int MAX_FIVES = 100;
  private static final int MAX_TENS = 50;

  private int[][][] mem;
  private int moneyFromStart = -1; // TODO: pass these to solve_rec instead
  private int cokesToBuy = -1;

  public Coke() {
    mem = new int[MAX_COKES+1][MAX_FIVES+10][MAX_TENS+1];

    for (int i = 0; i < MAX_COKES+1; i++) {
      for (int j = 0; j < MAX_FIVES+10; j++) {
        Arrays.fill(mem[i][j], -1);
      }
    }
  }
  
  /**
   * Returns the least number of coins to use given the number of cokes
   * to buy and the number of coins of values 1, 5, 10.
   *
   * Reuses state from previous calls to this method for efficiency.
   */
  public int solve(int numCokes, int numOnes, int numFives, int numTens) {
    this.moneyFromStart = numOnes + 5 * numFives + 10 * numTens;
    this.cokesToBuy = numCokes; 
    return solve(numCokes, numFives, numTens);
  }

  private int solve(
      final int numCokes, 
      final int numFives, 
      final int numTens) {
    
    // Base case
    if (numCokes == 0) {
      return 0;
    }

    // Check if already computed
    //if (mem[numCokes][numFives][numTens] != -1) {
    if (mem[numCokes][numFives][numTens] > 0) {
      return mem[numCokes][numFives][numTens];
    }
    
    this.mem[numCokes][numFives][numTens] = Integer.MAX_VALUE;
    int numCoins = Integer.MAX_VALUE;
    
    int numOnes = this.moneyFromStart - 8 * (this.cokesToBuy - numCokes) 
                                      - 5 * numFives 
                                      - 10 * numTens;
   
    // Now check all possible ways to pay for a coke with the current 
    // number of each coin value and find the least amount. Hardcoded for
    // a coke price of 8.
  
    if (numOnes >= 8) {
      numCoins = Math.min(
          numCoins, 
          8 + solve(numCokes - 1, numFives, numTens));
    }

    if (numFives >= 1 && numOnes >= 3) {
      numCoins = Math.min(
          numCoins, 
          4 + solve(numCokes - 1, numFives - 1, numTens));
    }
    
    if (numFives >= 2) {
      numCoins = Math.min(
          numCoins,
          2 + solve(numCokes - 1, numFives - 2, numTens));
    }

    if (numTens >= 1) {
      numCoins = Math.min(
          numCoins,
          1 + solve(numCokes - 1, numFives, numTens - 1));
    }
    
    // Assuming we can get a 5 back in this case (?)
    if (numTens >= 1 && numOnes >= 3) {
      numCoins = Math.min(
          numCoins,
          4 + solve(numCokes - 1, numFives + 1, numTens - 1));
    }
  
    this.mem[numCokes][numFives][numTens] = numCoins;
    return numCoins;
  }

  public static void main(String[] args) {
    final Scanner in = new Scanner(System.in);
    final int N = in.nextInt();
    final Coke cokeSolver = new Coke();
    for (int i = 0; i < N; i++) {
      System.out.println(cokeSolver.solve(
            in.nextInt(),
            in.nextInt(),
            in.nextInt(),
            in.nextInt()));
    }
  }
}
