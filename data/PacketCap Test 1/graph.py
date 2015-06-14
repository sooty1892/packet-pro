import numpy as np
import matplotlib.pyplot as plt

DATA1x = [1,2,3,4,5]
DATA1y = [1,2,3,4,5]
DATA2x = [1,2,3,4,5]
DATA2y = [2,3,4,5,6]
DATA3x = [1,2,3,4,5]
DATA3y = [3,4,5,6,7]

plt.plot(DATA1x, DATA1y, label="a") 
plt.plot(DATA2x, DATA2y, label="b")
plt.plot(DATA3x, DATA3y, label="c")



plt.xlabel('hello')
plt.ylabel('nanoseconds (logarithmic scale)')

plt.xlim(0, 6)
plt.ylim(0, 8)
plt.legend()



plt.tight_layout()
plt.show()
