from itertools import izip

"""
Idea:
    Sort A ascending, sort B ascending. The smallest in A should be matched
    with the largest in B.
"""

def solve(N, K, A, B):
    A.sort()
    B.sort(reverse=True)
    
    C = [a + b >= K for a, b in izip(A, B)]
    return "YES" if all(C) else "NO"

if __name__ == "__main__":
    T = int(raw_input())
    for _ in range(T):
        N, K = [int(i) for i in raw_input().split()]
        A = [int(i) for i in raw_input().split()]
        B = [int(i) for i in raw_input().split()]
        print(solve(N, K, A, B))
