package org.asam.ods;


/**
 * Generated from IDL interface "NameIterator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface NameIteratorOperations
{
	/* constants */
	/* operations  */
	void destroy() throws org.asam.ods.AoException;
	int getCount() throws org.asam.ods.AoException;
	java.lang.String[] nextN(int how_many) throws org.asam.ods.AoException;
	java.lang.String nextOne() throws org.asam.ods.AoException;
	void reset() throws org.asam.ods.AoException;
}
