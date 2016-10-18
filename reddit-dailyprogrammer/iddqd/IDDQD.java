import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import java.awt.Point;

public class IDDQD {

  // In this solution, consider the sets of possible line types
  // make it only dependent on the zombie positions instead, ie we dont need to check
  // lines that dont contain or are close to zombie positions. 
  // This makes the complexity based on number of zombies instead and should handle the big input
  // if sparse enough in terms of zombies.


  // TODO: something with sorting both row-wise and col-wise

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    final int H = in.nextInt();
    final int W = in.nextInt();
    while (in.hasNext()) {
      int h = in.nextInt();
      int w = in.nextInt();
    }
  }
}
