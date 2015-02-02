/**
 * DpdkInit.h
 *
 *  Created on: 15 Oct 2014
 *      Author: Abdul Alim <a.alim@imperial.ac.uk>
 */
#ifndef __DPDK_INIT__
#define __DPDK_INIT__

#include "RuntimeException.h"

/**
 * A singleton class to initialize Intel's DPDK framework.
 */
class DpdkInit {
  int m_cpu;
  static DpdkInit *INSTANCE;
  /**
	 * Private DpdkInit constructor to prevent object creation.
	 * It first initializes the DPDK framework and makes 
	 * sure that there is at least one one DPDK enabled 
	 * interface on the system. 
	 * It throws RuntimeException 
	 * if either DPDK initialization fails, or there is 
	 * no DPDK enabled interface on the systemi.
	 * @param argc - command-line argument count required by the DPDK init
	 * @param argv - command-line argument list required by the DPDK init
	 * @param cpu - the CPU number to be used to pin the main thread (currently unused)
	 */
  DpdkInit(int argc, char **argv, int cpu);
	/**
	 * Private copy constructor to prevent copying singleton object.
	 */
  DpdkInit(DpdkInit const&);
	/**
	 * Private assignment operator to prevent cloning singleton object.
	 */
  void operator=(DpdkInit const&);

 public:
	/**
	 * DpdkInit destructor. 
	 */
  ~DpdkInit();
	/**
	 * Static method to create the singleton object.
	 */
  static void init(int argc, char **argv, int cpu) {
    if (INSTANCE == NULL)
      INSTANCE = new DpdkInit(argc, argv, cpu);
  }
	/**
	 * Static method to access the singleton object. It throws a RuntimeException
	 * if the DPDK sytem was not initialized by calling DpdkMtcp::init().
	 * @return DpdkInit * - it returns a pointer to the singleton DpdkInit object.
	 */
  static DpdkInit *getInstance() {
    if (INSTANCE == NULL) throw new RuntimeException("DpdkInit() is not initialized.");
    return INSTANCE;
  }
};

#endif //__DPDK_INIT__
