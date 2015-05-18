make clean
javac Pktcap.java DpdkAccess.java
make
sudo cp /home/dpdk64/packet-pro/apps/pktcap/build/lib/libnat_dpdk.so /usr/lib/x86_64-linux-gnu
sudo ldconfig
