# Packet Processing in Java

### Repository Structure
presentation.pdf - presentation
setup.sh - script to install module, bind NICs and add huge pages. Will need modifying depending on system
apps - contains main DPDKJava framework and associated applications
	DPDKJava - main framework up until report
	DPDKJava-R - framework re-vamp after the report, with much faster packet handling and affinity thread handling
	c_apps - DPDK applications written in native language
		firewall - simple firewall implementation
		pktcapture - receives and frees packets as fast as possible
	java_apps - DPDK applications written in Java using DPDKJava
		Firewall - simple firewall implementation using config file and blacklist. mak.sh will make, compile and install required libraries
		PacketCap - receives and frees packets as fast as possible. mak.sh will make, compile and install required libraries
		old - contains an older version of PacketCap application not using framework
benchmark - testing programs for benchmarking for serialization, data access and comparison of language performance
	language - tests performance between C, Java and JNI
	test_data_access