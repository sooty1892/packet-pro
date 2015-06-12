make clean
#rm -f hs_err*
find -name "*.java" > sources.txt
find ../DpdkJava/ -name "*.java" >> sources.txt
javac -XDignore.symbol.file @sources.txt
rm -f sources.txt
make
sudo \cp /home/dpdk64/packet-pro/apps/PacketCap/build/lib/libnat_dpdk.so /usr/lib/x86_64-linux-gnu
sudo ldconfig

find ./src/  -name "*.class" > classes.txt
find ../DpdkJava/ -name "*.class" >> classes.txt
while read line
do
	cp $line ./app
done < classes.txt

cp src/config.properties ./app/

rm -r classes.txt
