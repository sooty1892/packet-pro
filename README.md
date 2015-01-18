# Packet Processing in Java

### Directory Structure
docs - Documentation including DPDK and related research papers

dpdk-1.7.1 - The used DPDK install files

example_code - Code obtained from various sources for reference

pktgen_dpdp-2.7.6 - A packet generation program to add packets directly into the NIC

playing_code - Simple code used to get to grips with various technology

### Installation
Remember to set enviromental variables RTE_SDK & RTE_TARGET:
```sh
$ export RTE_SDK=/home/testdpdk/dpdk-1.7.1
$ export RTE_TARGET=i686-native-linuxapp-gcc
```

### Setup for Oracle VirtualBoxVM
Bridged Adapter: Intel PRO/1000 MT Desktop (82540EM)

Required packages: git, hwloc, virtualbox-guest-dkms, vim

```sh
Allocate hugepages memory
$ echo 1024 > /sys/kernel/mm/hugepages/hugepages-2048kB/nr_hugepages

Make memory available for DPDK
$ mkdir /mnt/huge
$ mount -t hugetlbfs nodev /mnt/huge

The mount point can be made permanent across reboots, by adding the following line to the /etc/fstab file:
nodev /mnt/huge hugetlbfs  defaults 0 0

Load linux module into kernel
$ sudo modprobe uio
$ sudo insmod kmod/igb_uio.ko

Load VFIO module???
$ sudo modprobe vfio-pci

Check for available ports
$ ./tools/dpdk_nic_bind.py --status

Bind port to DPDK
$ ./tools/dpdk_nic_bind.py --bind=igb_uio eth1

Restore port to bind on kernel
$ ./tools/dpdk_nic_bind.py --bind=ixgbe 82:00.0

Set environment variables:  RTE_SDK & RTE_TARGET
```
