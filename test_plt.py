import matplotlib.pyplot as plt
from matplotlib.pyplot import MultipleLocator

x = [100, 200, 300, 400, 500, 600, 700, 800, 900, 1000]
x_ = [100, 200, 300, 400]
alg_3 = [2.1, 10.8, 47.6, 91.2, 169.8, 293.2, 479.3, 705.0, 983.0, 1347.9]
alg_4 = [2.2, 14.9, 61.3, 146.5, 276.6, 494.9, 837.3, 1164.0, 1788.5, 2399.6]
alg_5 = [2.9, 22.6, 75.4, 168.5, 346.9, 621.8, 985.0, 1518.8, 2182.1, 2948.7]
alg_6 = [2.8, 20.8, 65.0, 170.0, 357.3, 635.1, 852.9, 1467.6, 2275.4, 3174.4]
opt_3 = [202.8, 813.8, 1745.7, 2960.7, 4717.8, 6737.5, 9045.9, 12276.9, 15951.8, 19238.4]
opt_4 = [247.1, 852.4, 1947.8, 3147.8, 4784.2, 6763.9, 9691.4, 12435.4, 19270.5, 22521.7]
opt_5 = [1723.1, 14191.6, 172703.8, 131860902.5]
opt_6 = [201.6, 767.8, 4359.0, 7554.9, 11682.9, 16480.9, 23009.6, 30066.6, 38792.8, 48240.1]
plt.xlabel("n")
plt.ylabel("running time(ms)")
plt.plot(x, alg_3, lw=1, label='ALG(k=.3n)', color='#4169E1', marker='*')
plt.plot(x, alg_4, lw=1, label='ALG(k=.4n)', color='green', marker='+')
plt.plot(x, alg_5, lw=1, label='ALG(k=.5n)', color='orange', marker='o')
plt.plot(x, alg_6, lw=1, label='ALG(k=.6n)', color='black', marker='x')
plt.plot(x, opt_3, lw=1, label='OPT(k=.3n)', color='red', marker='s')
plt.plot(x, opt_4, lw=1, label='OPT(k=.4n)', color='skyblue', marker='d')
plt.plot(x_, opt_5, lw=1, label='OPT(k=.5n)', color='#F08080', marker='p')
plt.plot(x, opt_6, lw=1, label='OPT(k=.6n)', color='#808000', marker='^')
x_locator = MultipleLocator(100)
# y_locator = MultipleLocator(100)
ax = plt.gca()
ax.xaxis.set_major_locator(x_locator)
ax.set_yscale("log", nonposy='clip')
plt.xlim(100, 1000)
plt.ylim(bottom=1)
plt.legend()
plt.show()

