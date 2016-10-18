import heapq as PQ

INF = float("inf") # TODO: or just use constraints

class DijskstraSolver(object):
    def __init__(self, N, edges):
        print(edges)

    def _distances_from(self, source):
        dist = [-1] * self.N
        prev = [None] * self.N

        dist[source] = 0

        pq = []
        for v in range(N):
            if v != source:
                dist[v] = INF
            PQ.heappush(pq, (dist[v], v))

        while pq:
            u = PQ.heappop(pq)[1]

    def shortest_distance(self, x, y):
        return 0

class FloydSolver(object):
    def __init__(self, N, edges):
        print(edges)

    def shortest_distance(self, x, y):
        return 0

if __name__ == "__main__":
    N, M = map(int, raw_input().split())
    read_vertex = lambda s: int(s) - 1
    edges = [tuple(map(read_vertex, raw_input().split())) for _ in range(M)]
    
    solver = DijsktraSolver(N, edges)
    #solver = FloydSolver(N, edges)
    
    Q = int(raw_input())
    for _ in range(Q):
        x, y = map(read_vertex, raw_input().split())
        print(solver.shortest_distance(x, y)) 

