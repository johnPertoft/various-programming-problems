
def intersection(rock1, rock2):
    gems = {g for g in rock1}
    return [g for g in rock2 if g in gems]

def solve(rocks):
    I = reduce(lambda gems, rock: intersection(gems, rock), rocks[1:], rocks[0])
    return len(I)

if __name__ == "__main__":
    N = int(raw_input())
    rocks = [set(raw_input()) for _ in range(N)]
    print(solve(rocks))
