import numpy as np
import matplotlib.pyplot as plt


n_groups = 4

DATA = [11049, 97, 24, 23]
DATA2 = np.log10(DATA)
RATIO = [x/float(DATA[3]) for x in DATA]
RoundedRatio = [ '%.2f' % elem for elem in RATIO]

fig, ax = plt.subplots()

index = np.arange(n_groups)
bar_width = 0.5


chart1 = plt.bar(0, 
				 DATA2[0], 
				 bar_width,
                 color='b',
                 alpha=0.8)

chart2 = plt.bar(1, 
				 DATA2[1], 
				 bar_width,
                 color='b',
                 alpha=0.8)

chart3 = plt.bar(2,
				 DATA2[2], 
				 bar_width,
                 color='b',
                 alpha=0.8)

chart4 = plt.bar(3, 
				 DATA2[3], 
				 bar_width,
                 color='b',
                 alpha=0.8)



#lt.xlabel('xLABEL')
plt.ylabel('nanoseconds (logarithmic scale)')
# plt.title('Server')
plt.xticks(index + (bar_width/2), ('Object Passing', 'Byte Buffer', 'Unsafe', 'Direct'))
ax.set_yticks([0,1,2,3,4, 5])
ax.set_yticklabels(('0', '10', '100', '1000', '10000', '100000'))
plt.xlim(-(bar_width), index[-1]+2*bar_width)
plt.ylim(0, max(DATA2) + max(DATA2) / 10)
# # plt.legend()

barlabels = RoundedRatio


def autolabel(chart):
	for i,bar in enumerate(chart):
	        height = bar.get_height()
	        plt.text(bar.get_x()+bar.get_width()/2., 1.02*height, '%s'% (barlabels[i]),
	                ha='center', va='bottom')

height = chart1[0].get_height();
plt.text(chart1[0].get_x()+chart1[0].get_width()/2., 1.02*height, '%s'% (barlabels[0]),
	                ha='center', va='bottom')

height = chart2[0].get_height();
plt.text(chart2[0].get_x()+chart2[0].get_width()/2., 1.02*height, '%s'% (barlabels[1]),
	                ha='center', va='bottom')

height = chart3[0].get_height();
plt.text(chart3[0].get_x()+chart3[0].get_width()/2., 1.02*height, '%s'% (barlabels[2]),
	                ha='center', va='bottom')

height = chart4[0].get_height();
plt.text(chart4[0].get_x()+chart4[0].get_width()/2., 1.02*height, '%s'% (barlabels[3]),
	                ha='center', va='bottom')



# autolabel(chart1)
# autolabel(chart2)
# autolabel(chart3)
# autolabel(chart4)


plt.tight_layout()
plt.show()
