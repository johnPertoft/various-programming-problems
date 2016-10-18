import sys
from collections import deque

X_LIMIT = 100

def solve(N, L):
    C = [None] * X_LIMIT 
    for x, s in L:
        if C[x]:
            C[x].append(s)
        else:
            C[x] = deque([s])

    for x in range(X_LIMIT):
        if C[x]:
            for s in C[x]:
                sys.stdout.write(s + " ")

    sys.stdout.flush()

if __name__ == "__main__":
    N = int(raw_input())
   
    to_tup = lambda i, row: (int(row[0]), row[1] if i >= N/2 else "-")
    L = [to_tup(i, raw_input().split()) for i in range(N)]

    solve(N, L)
