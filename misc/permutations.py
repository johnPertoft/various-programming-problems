import math

# Print all permutations of s
S = "abcdefg"

def permute(s):
    if len(s) == 1:
        return [s]
    else:
        permutations = []
        for i in range(len(s)):
            c = s[i]
            perms = permute(s[:i] + s[i+1:])
            # Everyone gets a chance to be first
            permutations.extend(map(lambda p: c + p, perms))

        return permutations

permutations = permute(S)
assert len(permutations) == math.factorial(len(S))
assert len(set(permutations)) == len(permutations)
