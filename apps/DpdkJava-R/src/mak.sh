make clean
javac -XDignore.symbol.file *.java
make
sudo \cp ~/packet-pro/apps/DpdkJava-R/src/build/lib/libnat_dpdk.so /usr/lib/x86_64-linux-gnu
sudo ldconfig