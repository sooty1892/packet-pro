/**
 * RuntimeException.h
 *
 *  Created on: 10 Oct 2014
 *      Author: Abdul Alim <a.alim@imperial.ac.uk>
 */

#ifndef __RUNTIME_EXCEPTION__
#define __RUNTIME_EXCEPTION__

#include <string>
#include <exception>

/**
 * A simple exception class to send failure messages
 * between classes at runtime.
 */
class RuntimeException : public std::exception {
  const char *m_message;
 public:
	/**
	 * Constructor to create a RuntimeException object 
	 * with a description.
	 * @param msg - a string that describes the runtime failure scenario.
	 */
  RuntimeException(std::string msg) : m_message(msg.c_str()) { }
	/**
	 * Retrieves the descriptive message of the exception.
	 * @return char * - it returns a pointer to the char buffer 
	 * containing the description of the exception scenario.
	 */
	const char *what() const throw() { return m_message; }
};

#endif // __RUNTIME_EXCEPTION__

