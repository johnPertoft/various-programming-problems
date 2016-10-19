
"""
Problem: Output all possible ways to put +, -, or nothing between
numbers 1 to 9 to get 100 as result. I.e. 1 + 2 - 34 + ... = 100

Idea: Basic recursion, small data -> no need for memoize or dp 
1 + combinations_of(2 to 9),
1 - combinations_of(2 to 9),
combinations_of(12, 3 to 9)
"""

def solve_rec(numbers, target_sum, s):
    # Base case
    if len(numbers) == 1:
        if numbers[0] == target_sum:
            print("{} + {}".format(s, numbers[0]))
        return
    
    a = numbers[0]

    new_s = "{} + {}".format(s, a) if s else str(a)

    # Case 1
    solve_rec(numbers[1:], target_sum - a, new_s)

    # Case 2
    ns = numbers[1:]
    ns[0] *= -1
    solve_rec(ns, target_sum - a, new_s)

    # Case 3
    concat = int(str(a) + str(numbers[1]))
    solve_rec([concat] + numbers[2:], target_sum, s)


def solve(numbers):
    solve_rec(numbers, 100, "")

if __name__ == "__main__":
    solve(list(range(1, 10)))
