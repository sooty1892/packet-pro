# Packet Processing in Java

### Repository Structure

presentation.pdf - presentation

setup.sh - script to install module, bind NICs and add huge pages. Will need modifying depending on system

apps/ - contains main DPDKJava framework and associated applications

  - DPDKJava/ - main framework up until report
  - DPDKJava-R/ - framework re-vamp after the report, with much faster packet handling and affinity thread handling
  - c_apps/ - DPDK applications written in native language
    - firewall/ - simple firewall implementation
    - pktcapture/ - receives and frees packets as fast as possible
  - java_apps/ - DPDK applications written in Java using DPDKJava
    - Firewall/ - simple firewall implementation using config file and blacklist. mak.sh will make, compile and install required libraries
    - PacketCap/ - receives and frees packets as fast as possible. mak.sh will make, compile and install required libraries
    - old/ - contains an older version of PacketCap application not using framework
		
benchmark/ - testing programs for benchmarking for serialization, data access and comparison of language performance
  - data_access/ - tests for sharing data between Java and C
  - language/ - tests performance between C, Java and JNI
  - serializing/ - tests for speed of serializing data on Java side

data/ - contains all test data
  - firewall/ - firewall implementation results for Java and C
  - firewall_r/ - firewall implementation results using DPDKJava-R framework for Java
  - pctest1/ - packet capture results for DPDKJava with no improvements
  - pctest2/ - packet capture results for DPDKJava after 1st improvements
  - pctest3/ - packet capture results for DPDKJava after 2nd improvements
  - pctest4/ - packet capture results for DPDKJava after 3rd improvements
  - pctest_r/ - packet capture results using DPDKJava-R
  - pktcap/ - packet capture results using C implementation

docs/ - collection of useful documentation

dpdk-2.0.0/ - DPDK as produced by Intel. Includes some custom minor changes to structs to make them packed. DPDKJava and DPDKJava-R will only work with this

example_code/ - some code provided by Abdul Alim for basic set-up of applications and shared libraries

pkygen-2.8.6/ - packet generator application which uses DPDK

playing-code/ - code examples used to get to grips with JNI, DPDK, byte buffers etc

reports/ - final and interim reports in latex
