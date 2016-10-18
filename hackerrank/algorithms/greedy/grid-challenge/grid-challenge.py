
def solve(N, G):
  G = [sorted(row) for row in G]
  
  sorted_rows = all([all(G[r][i] <= G[r][i+1] for i in range(N-1)) for r in range(N)])
  
  sorted_cols = all([all(G[i][c] <= G[i+1][c] for i in range(N-1)) for c in range(N)])

  return "YES" if sorted_rows and sorted_cols else "NO" 

if __name__ == "__main__":
  T = int(raw_input())
  for _ in range(T):
    N = int(raw_input())
    G = [[c for c in raw_input()] for _ in range(N)]
    print(solve(N, G))
