import sys

"""
Constraints:
    1 <= A_i <= 1e9
    1 <= P <= Q <= 1e9

Idea:
    iterate over possible M, use binary search to find closest one in A. 
    The closest gives the smallest diff for that M. Check if it's a bigger min 
    than previous.
"""

def naive(N, P, Q, A):
    best_M = P
    max_min_diff = -sys.maxint - 1

    for M in range(P, Q + 1):
        min_diff = sys.maxint
        for a in A:
            diff = abs(a - M)
            if diff < min_diff:
                min_diff = diff

        if min_diff > max_min_diff:
            max_min_diff = min_diff
            best_M = M

    return best_M

def solve(N, P, Q, A):
    A.sort()
    
    # TODO: could use that we always go "left" in this binary search in the
    # beginning
    
    # Returns the index of the element closest to M    
    def bs_closest(low, high, M):
        while low < high:
            mid = low + (high - low) / 2
            diff1 = abs(A[mid] - M)
            diff2 = abs(A[mid+1] - M)
            if diff2 <= diff1:
                low = mid + 1
            else:
                high = mid

        return high
        
    best_M = P
    max_min_diff = -sys.maxint - 1
    start_idx = 0
    for M in range(P, Q + 1):
        i = bs_closest(start_idx, len(A) - 1, M)
        start_idx = i
        min_diff = abs(A[i] - M)
        if min_diff > max_min_diff:
            max_min_diff = min_diff
            best_M = M

    return best_M

if __name__ == "__main__":
   N = int(raw_input())
   As = [int(d) for d in raw_input().split()]
   P, Q = [int(d) for d in raw_input().split()]

   print(solve(N, P, Q, As))
