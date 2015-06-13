# clearing hugepages
echo > .echo_tmp
for d in /sys/devices/system/node/node? ; do
	echo "echo 0 > $d/hugepages/hugepages-2048kB/nr_hugepages" >> .echo_tmp
done
echo "Removing currently reserved hugepages"
sudo sh .echo_tmp
rm -f .echo_tmp

# removing and unmount hugepages
grep -s '/mnt/huge' /proc/mounts > /dev/null
if [ $? -eq 0 ] ; then
	sudo umount /mnt/huge
fi
if [ -d /mnt/huge ] ; then
	sudo rm -R /mnt/huge
fi

# set non numa pages
echo "echo 128 > /sys/kernel/mm/hugepages/hugepages-2048kB/nr_hugepages" > .echo_tmp
echo "Reserving new hugepages"
sudo sh .echo_tmp
rm -f .echo_tmp

# create and mount hugepapes
sudo mkdir -p /mnt/huge
grep -s '/mnt/huge' /proc/mounts > /dev/null
if [ $? -ne 0 ] ; then
	sudo mount -t hugetlbfs nodev /mnt/huge
fi
