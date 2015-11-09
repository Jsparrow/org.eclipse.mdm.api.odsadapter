package org.asam.ods;


/**
 * Generated from IDL interface "ElemResultSetExtSeqIterator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface ElemResultSetExtSeqIteratorOperations
{
	/* constants */
	/* operations  */
	void destroy() throws org.asam.ods.AoException;
	int getCount() throws org.asam.ods.AoException;
	org.asam.ods.ElemResultSetExt[] nextN(int how_many) throws org.asam.ods.AoException;
	org.asam.ods.ElemResultSetExt nextOne() throws org.asam.ods.AoException;
	void reset() throws org.asam.ods.AoException;
}
