from collections import defaultdict

root = [{}, 0]

def add(s):
    current_node = root
    for c in s:
        children = current_node[0]
        if not c in children.keys():
            children[c] = [{}, 0]
        
        # TODO: should not update if adding same word twice

        # Go to next node
        current_node = children[c]
        current_node[1] += 1

def find(s):
    current_node = root
    for i in range(len(s)):
        children = current_node[0]
        if s[i] not in children.keys():
            return 0
        else:
            current_node = children[s[i]]

    return current_node[1]

"""
ALPHABET_START = ord("a")
ALPHABET_SIZE = ord("z") - ord("a") + 1

root = [[None] * ALPHABET_SIZE, 0]

def add(s):
    current_node = root
    for c in map(lambda x: ord(x) - ALPHABET_START, s):
        children = current_node[0]
        if not children[c]:
            children[c] = [[None] * ALPHABET_SIZE, 0]
        
        current_node = children[c]
        current_node[1] += 1

def find(s):
    current_node = root
    for c in map(lambda x: ord(x) - ALPHABET_START, s):
        children = current_node[0]
        if not children[c]:
            return 0
        else:
            current_node = children[c]
    
    return current_node[1]
"""

if __name__ == "__main__":
    N = int(raw_input())
    for _ in range(N):
        command, s = raw_input().split()
        if command == "add":
            add(s)
        else:
            print(find(s))
