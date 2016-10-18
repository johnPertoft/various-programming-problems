# dp solution? dp matrix with (day, numstocks)?
# at one point, the optimal choice is either to buy one or sell all? (is it ever to just sell some?)


# have unlimited money so DP not needed I think
# cant buy and sell at the same time

def backwards_pass(N, prices):
  maxP = 0
  profit = 0
  for i in range(N-1, -1, -1):
    if prices[i] >= maxP:
      maxP = prices[i]
    
    profit += maxP - prices[i]
  
  return profit

if __name__ == "__main__":
  T = int(raw_input())
  for _ in range(T):
    N = int(raw_input())
    prices = map(int, raw_input().split())
    
    print(backwards_pass(N, prices))
