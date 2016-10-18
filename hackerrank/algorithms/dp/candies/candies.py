def solve(N, R):
    if N <= 1:
        return N

    C = [1] * N
    
    for i in range(1, N):
        if R[i] > R[i-1]:
            C[i] = C[i-1] + 1

    for i in range(N-1, 0, -1):
        if R[i-1] > R[i]:
            C[i-1] = max(C[i]+1, C[i-1])
    
    return sum(C)

if __name__ == "__main__":
    N = int(raw_input())
    R = [int(raw_input()) for _ in range(N)]

    print(solve(N, R))
