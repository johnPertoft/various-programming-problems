
def solve(A, B, N):
    for i in range(N - 2):
        A, B = B, B**2 + A

    return B

if __name__ == "__main__":
    A, B, N = map(int, raw_input().split())
    print(solve(A, B, N))
