
"""

base cases:
    |A| = 1


need to know if there is a subset of A that sums to A_sum/2

has_subset_sum(): either some element is part of the

possibly useable
    odd sum -> cant be partitioned
"""

def has_subset_sum(A, i, s):
    if s == 0:
        return True
    if s < 0:
        return False
    if i == 0:
        return A[0] == s

    return has_subset_sum(A, i-1, s) or has_subset_sum(A, i-1, s - A[i])

def get_subsets(A, s):
    subsets = []

    def traverse(A, i, s, ss):
        if s == 0:
            subsets.append(ss)
        elif s < 0:
            return
        elif i == 0:
            if A[0] == s:
                subsets.append([A[0]] + ss)
        else:
            traverse(A, i-1, s, ss)
            traverse(A, i-1, s - A[i], ss + [A[i]])

    traverse(A, len(A) - 1, s, [])
    return subsets

def solve(N, A):
    s = sum(A)
# TODO: need to find all possible partitions

if __name__ == "__main__":
    T = int(raw_input())
    for _ in range(T):
        N = int(raw_input())
        A = map(int, raw_input().split())
        print(solve(N, A))

    A = [1, 21, 3, 2, 2, 4]
    #print(has_subset_sum(A, len(A)-1, 4))
    ss = get_subsets(A, 4)
    print(ss)
