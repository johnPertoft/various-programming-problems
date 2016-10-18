
"""
Idea:
  At each point, we either put down a 1 or a 4 then sum these for
  number of ways. Prime part can be solved with sieve of eratsosotjtohe

"""

PRIME_LIMIT = 218000
sieve = [True] * PRIME_LIMIT
sieve[0] = False
sieve[1] = False
for p in xrange(3, PRIME_LIMIT, 2):
  if not sieve[p]:
    continue

  for q in xrange(p+p, PRIME_LIMIT, p):
    sieve[q] = False

def num_primes(M):
  if M < 2:
    return 0

  num_primes = 1 # Starting with 1 because 2 is prime
  for p in range(3, M+1, 2):
    if sieve[p]:
      num_primes += 1
  return num_primes

mem = {}
def solve(N):
  def _solve(N):
    if N <= 3:
      return 1
    elif N in mem:
      return mem[N]
    else:
      ways = _solve(N-1) + _solve(N-4)
      mem[N] = ways
      return ways
  
  ways = _solve(N)
  return num_primes(ways)

if __name__ == "__main__":
  T = int(raw_input())
  for _ in range(T):
    N = int(raw_input())
    print(solve(N))
