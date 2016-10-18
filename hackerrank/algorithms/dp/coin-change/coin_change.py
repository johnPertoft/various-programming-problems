"""
Constraints:
    1 <= C_i <= 50
    1 <= M <= 50
    1 <= N <= 250
    given coin list is unique
    infinitely many of each value

Idea:
    At a point where there is N money and [coin values], there are two cases. 
    Either the first coin value is part of the way to change or it is not. So
    recursion happens to split this into subproblems. One where the coin value
    is used to subtract from the N money -> (N-coinvalue) and one where N
    remains the same but we don't use the first coin value.
"""

def simple(money, coins):
    if money == 0:
        return 1
    elif not coins or money < 1:
        return 0
    else:
        return simple(money - coins[0], coins) + simple(money, coins[1:])

def solve(N, M, coins):
    mem = []
    for i in range(N+1):
        mem.append([None] * M)

    def change(money, coin_idx):
        if money == 0:
            return 1
        elif coin_idx >= M or money < 0:
            return 0
        elif mem[money][coin_idx] != None:
            return mem[money][coin_idx]
        else:
            case1 = change(money, coin_idx + 1)
            case2 = change(money - coins[coin_idx], coin_idx)
            mem[money][coin_idx] = case1 + case2
            return mem[money][coin_idx]

    #return simple(N, coins)
    return change(N, 0)

if __name__ == "__main__":
    N, M = [int(n) for n in raw_input().split()]
    coins = [int(c) for c in raw_input().split()]
    
    print(solve(N, M, coins))
