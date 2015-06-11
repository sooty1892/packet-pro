import numpy as np
import matplotlib.pyplot as plt


n_groups = 4

DATA = [22, 23, 73, 14254]
DATA2 = np.log10(DATA)
RATIO = [x/float(DATA[0]) for x in DATA]
RoundedRatio = [ '%.2f' % elem for elem in RATIO]
loglist = [0, 10, 100, 1000, 10000, 100000]
fig, ax = plt.subplots()

index = np.arange(n_groups)
bar_width = 0.35


chart1 = plt.bar(index, 
				 DATA2, 
				 bar_width,
                 color='b')



# plt.xlabel('xLABEL')
plt.ylabel('nanoseconds (logarithmic scale)')
plt.title('Server')
plt.xticks(index + (bar_width/2), ('Direct', 'Unsafe', 'ByteBuffer', 'Object'))
ax.set_yticks([0,1,2,3,4, 5])
ax.set_yticklabels(('0', '10', '100', '1000', '10000', '100000'))
plt.xlim(-(bar_width), index[-1]+2*bar_width)
plt.ylim(0, max(DATA2) + max(DATA2) / 10)
# plt.legend()

barlabels = RoundedRatio


def autolabel(chart):
	for i,bar in enumerate(chart):
	        height = bar.get_height()
	        plt.text(bar.get_x()+bar.get_width()/2., 1.02*height, '%s'% (barlabels[i]),
	                ha='center', va='bottom')
	  



autolabel(chart1)


plt.tight_layout()
plt.show()
