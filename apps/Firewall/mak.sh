make clean
rm -f hs_err*
javac -XDignore.symbol.file *.java /firewall/*.java
make
sudo \cp /home/dpdk64/packet-pro/apps/generic/build/lib/libnat_dpdk.so /usr/lib/x86_64-linux-gnu
sudo ldconfig
