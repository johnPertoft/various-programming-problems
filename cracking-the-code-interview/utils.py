from timeit import timeit

def times(n, fn):
    assert n >= 0
    for _ in range(n):
        fn()
        
def timeit_and_return(fn):
    return (fn(), timeit(fn, number=10) / 10)