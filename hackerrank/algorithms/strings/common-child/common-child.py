
"""
Idea:
  This is "just" the longest common subsequence problem.

"""

def solve(a, b):
  m = len(a)
  n = len(b)

  LCS = []
  for i in range(m+1):
    LCS.append([0] * (n+1))
  
  for i in range(1, m+1):
    for j in range(1, n+1):
      if a[i-1] == b[j-1]:
        LCS[i][j] = LCS[i-1][j-1] + 1
      else:
        LCS[i][j] = max(LCS[i][j-1], LCS[i-1][j])
 
  return LCS[m][n] 

if __name__ == "__main__":
  a, b = raw_input(), raw_input()
  print(solve(a, b))
