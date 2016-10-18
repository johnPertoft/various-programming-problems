def solve(M, N, marsh):
    right = [[0] * N for _ in range(M)]
    down = [[0] * N for _ in range(M)]
    
    # Precompute how far to the right you can from each
    # position without hitting a marsh.
    for row in range(M):
        for col in range(N-2, -1, -1):
            if marsh[row][col]:
                right[row][col] = -1
            else:
                right[row][col] = right[row][col+1] + 1

    # Precompute how far down you can from each position 
    # without hitting a marsh.
    for col in range(N):
        for row in range(M-2, -1, -1):
            if marsh[row][col]:
                down[row][col] = -1
            else:
                down[row][col] = down[row+1][col] + 1
    
    best_perimeter = -1
    for row in range(M-1):
        for col in range(N-1):
            if marsh[row][col]:
                continue

            max_row2 = row + down[row][col]
            max_col2 = col + right[row][col]
            
            if best_perimeter >= (max_row2-row) * 2 + (max_col2-col) * 2:
                continue
            
            for row2 in range(max_row2, row, -1):
                for col2 in range(max_col2, col, -1):
                    if marsh[row2][col2]:
                        continue
                    
                    # Check both horizontal lines   
                    if (right[row][col] < col2 - col or
                        right[row2][col] < col2 - col):
                        continue

                    # Check both vertical lines
                    if (down[row][col] < row2 - row or
                        down[row][col2] < row2 - row):
                        continue

                    # All lines are ok
                    new_perimeter = (row2 - row) * 2 + (col2 - col) * 2
                    if new_perimeter > best_perimeter:
                        best_perimeter = new_perimeter
                    
                    # Perimeters after this one in loops over row2,col2
                    # will be smaller so we can break out of them at this 
                    # point?
                    # TODO

#===============================

    return best_perimeter if best_perimeter > 0 else "impossible"

if __name__ == "__main__":
    M, N = map(int, raw_input().split())
    marsh = [[r != "." for r in raw_input()] for _ in range(M)]
    print(solve(M, N, marsh))

# TODO: check rectangles from large to small until ok instead?
