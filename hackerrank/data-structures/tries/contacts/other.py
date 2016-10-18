root = [{},0]
fixed_root = root

def add(root, word,level):
    if level < word.__len__():
        if word[level] not in root[0].iterkeys():
            root[0][word[level]] = [{},0]
            root[0][word[level]][1] = root[0][word[level]][1]+1
            add(root[0][word[level]], word, level+1)
        else:
            root[0][word[level]][1] = root[0][word[level]][1]+1
            add(root[0][word[level]],word, level+1)
    else:
        root[0]['/'] = None

def frequency_prefix(root, word, level):
    while level< word.__len__():
        if word[level] in root[0].iterkeys():
            root = root[0][word[level]]
            level +=1
        else:
            return 'not_found'
    return root

def stored_count(root):
    return root[1]

def find(root, word, level):
    f_p = frequency_prefix(root, word, level)
    if f_p == 'not_found':
        return 0
    else:
        return stored_count(f_p)

N = int(raw_input().strip())
for testcase in xrange(N):
    fun, word = map(str, raw_input().strip().split(' '))
    if fun == 'add':
        add(fixed_root, word, 0)
    if fun == 'find':
        print find(fixed_root, word, 0)

