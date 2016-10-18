from collections import deque, defaultdict

EDGE_LENGTH = 6

def solve(N, edges, S):
    E = defaultdict(list)
    for a, b in edges:
        E[a].append(b)
        E[b].append(a)
   
    distances = [-1] * (N + 1)
    distances[S] = 0
    q = deque([S])
    while q:
        s = q.popleft()
        for t in E[s]:
            if distances[t] == -1:
                distances[t] = distances[s] + EDGE_LENGTH
                q.append(t)
    
    print(" ".join([str(distances[i]) for i in range(1, S) + range(S+1, N+1)]))


if __name__ == "__main__":
    T = int(raw_input())
    for _ in range(T):
        N, E = map(int, raw_input().split())
        edges = [tuple(map(int, raw_input().split())) for _ in range(E)]
        S = int(raw_input())
        solve(N, edges, S)
