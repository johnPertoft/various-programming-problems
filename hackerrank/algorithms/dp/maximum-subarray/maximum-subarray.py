
"""
Idea:
    For contiguous, at each index we can either continue from previous
    largest contiguous subarray sum or start a new subarray.

    For non contigous, at each index we can choose whether that index 
    should be part of the subarray or not, or start a new subarray.
"""

def solve(N, A):
    cont = [None] * N
    cont[0] = A[0]
    non_cont = [None] * N
    non_cont[0] = A[0]

    for i in range(1, N):
        cont[i] = max(cont[i-1] + A[i], A[i])
        non_cont[i] = max(non_cont[i-1], non_cont[i-1] + A[i], A[i])

    return max(cont), non_cont[N-1]

if __name__ == "__main__":
    T = int(raw_input())
    for _ in range(T):
        N = int(raw_input())
        A = map(int, raw_input().split())
        contiguous, non_contiguous = solve(N, A)
        print("{} {}".format(contiguous, non_contiguous))
