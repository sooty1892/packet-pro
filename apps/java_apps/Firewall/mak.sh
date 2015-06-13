make clean
#rm -f hs_err*
find -name "*.java" > sources.txt
find ../DpdkJava/ -name "*.java" >> sources.txt
javac -XDignore.symbol.file @sources.txt
rm -f sources.txt
make
sudo \cp ~/packet-pro/apps/java_apps/Firewall/build/lib/libnat_dpdk.so /usr/lib/x86_64-linux-gnu
sudo ldconfig

find ./src/  -name "*.class" > classes.txt
find ../DpdkJava/ -name "*.class" >> classes.txt
while read line
do
	cp $line ./app
done < classes.txt

cp src/blacklist.txt ./app/
cp src/config.properties ./app/

rm -r classes.txt
