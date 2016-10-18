
def solve_memo(num_dims, dims, num_steps, pos):
  pos = [p - 1 for p in pos] # 0-indexing
  
  mem = {}

  def state_str(steps, pos):
    return str(steps) + " " + " ".join(map(str, pos))

  def grid_walk(steps, pos):
    ss = state_str(steps, pos)
    if ss in mem:
      return mem[ss]
    
    if steps == 1:
      ways = 0
      for i in range(num_dims):
        if pos[i] + 1 < dims[i]:
          ways += 1
        if pos[i] - 1 >= 0:
          ways += 1
      mem[ss] = ways
      return ways
    else:
      ways = 0
      for i in range(num_dims):
        if pos[i] + 1 < dims[i]:
          pos[i] += 1
          ways += grid_walk(steps - 1, pos)
          pos[i] -= 1
        if pos[i] - 1 >= 0:
          pos[i] -= 1
          ways += grid_walk(steps - 1, pos)
          pos[i] += 1
      mem[ss] = ways
      return ways
  
  return grid_walk(num_steps, pos)

def solve(num_dims, dims, num_steps, pos):
  pos = [p - 1 for p in pos] # 0-indexing
  # need different approach
  # Do dp algorithm per dimension, then combine the solutions
  # for each dimension in a smart way? 
  return None

if __name__ == "__main__":
  T = int(raw_input())
  for _ in range(T):
    num_dims, num_steps = [int(c) for c in raw_input().split()]
    pos = [int(c) for c in raw_input().split()]
    dims = [int(c) for c in raw_input().split()]
    modways = solve(num_dims, dims, num_steps, pos) % 1000000007
    print(modways)
