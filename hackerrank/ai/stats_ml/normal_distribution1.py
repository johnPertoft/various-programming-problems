from scipy.stats import norm

N = lambda x: norm.cdf(x, loc = 30, scale = 4)

print("%.3f" % N(40))           # P(x < 40)
print("%.3f" % (1.0 - N(21)))   # P(x > 21)
print("%.3f" % (N(35) - N(30))) # P(30 < x < 35)
