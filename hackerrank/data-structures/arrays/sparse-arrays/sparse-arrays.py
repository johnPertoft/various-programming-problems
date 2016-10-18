from collections import Counter

if __name__ == "__main__":
    N = int(raw_input())
    strings = [raw_input() for _ in range(N)]
    counts = Counter(strings)
    Q = int(raw_input())
    for _ in range(Q):
        print(counts[raw_input()])
