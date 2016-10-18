from collections import deque
import sys

"""
Idea:
    Do a BFS/DFS at each vertex (coordinate), for each visit assign an
    increasing number. Restart for each clique.
"""

dirs = [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)]

def solve(rows, cols, M):
    largest_region_size = -sys.maxint - 1
    visited = [[False] * cols for _ in range(rows)]

    def start_dfs(row, col):
        region_size = [0] # lol python
        def dfs(r, c):
            if visited[r][c] or M[r][c] == 0:
                return
            else:
                visited[r][c] = True
                region_size[0] += 1
                for dx, dy in dirs:
                    rn = r + dy
                    cn = c + dx
                    if not (rn < 0 or rn >= rows or cn < 0 or cn >= cols):
                        dfs(rn, cn)

        dfs(row, col)
        return region_size[0]

    for row in range(rows):
        for col in range(cols):
            if M[row][col] == 0 or visited[row][col]:
                visited[row][col] = True
                continue
            else:
                sz = start_dfs(row, col)
                if sz > largest_region_size:
                    largest_region_size = sz

    return largest_region_size

if __name__ == "__main__":
    rows, cols = int(raw_input()), int(raw_input())
    M = [[int(c) for c in raw_input().split()] for _ in range(rows)] 

    print(solve(rows, cols, M))
