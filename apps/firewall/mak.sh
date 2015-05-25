make clean
javac ApplicationStarter.java Utils.java DpdkAccess.java
make
sudo \cp /home/dpdk64/packet-pro/apps/firewall/build/lib/libnat_dpdk.so /usr/lib/x86_64-linux-gnu
sudo ldconfig
