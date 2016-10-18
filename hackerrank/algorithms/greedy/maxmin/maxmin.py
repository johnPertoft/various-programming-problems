import sys

"""
Idea:
    The most "fair" subset should be the ones closest right?
    So we just sort first and then pick the best continuous subset.
    Also, since it's sorted the min value is just the left most value
    and max value is the rightmost value.

"""

def solve(N, K, Ns):
    Ns.sort()
    min_unfairness = sys.maxint
    for start in range(len(Ns) - K + 1):
        unfairness = Ns[start+K-1] - Ns[start]
        if unfairness < min_unfairness:
            min_unfairness = unfairness
        
    return min_unfairness

if __name__ == "__main__":
    N = int(raw_input())
    K = int(raw_input())
    Ns = [int(raw_input()) for _ in range(N)]

    print(solve(N, K, Ns))
