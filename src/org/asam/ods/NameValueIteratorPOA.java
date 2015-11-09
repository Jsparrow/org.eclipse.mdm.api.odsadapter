package org.asam.ods;


/**
 * Generated from IDL interface "NameValueIterator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class NameValueIteratorPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.NameValueIteratorOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "nextOne", new java.lang.Integer(0));
		m_opsHash.put ( "nextN", new java.lang.Integer(1));
		m_opsHash.put ( "reset", new java.lang.Integer(2));
		m_opsHash.put ( "getCount", new java.lang.Integer(3));
		m_opsHash.put ( "destroy", new java.lang.Integer(4));
	}
	private String[] ids = {"IDL:org/asam/ods/NameValueIterator:1.0"};
	public org.asam.ods.NameValueIterator _this()
	{
		return org.asam.ods.NameValueIteratorHelper.narrow(_this_object());
	}
	public org.asam.ods.NameValueIterator _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.NameValueIteratorHelper.narrow(_this_object(orb));
	}
	public org.omg.CORBA.portable.OutputStream _invoke(String method, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler)
		throws org.omg.CORBA.SystemException
	{
		org.omg.CORBA.portable.OutputStream _out = null;
		// do something
		// quick lookup of operation
		java.lang.Integer opsIndex = (java.lang.Integer)m_opsHash.get ( method );
		if ( null == opsIndex )
			throw new org.omg.CORBA.BAD_OPERATION(method + " not found");
		switch ( opsIndex.intValue() )
		{
			case 0: // nextOne
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.NameValueHelper.write(_out,nextOne());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // nextN
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				org.asam.ods.NameValueSequenceHelper.write(_out,nextN(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // reset
			{
			try
			{
				_out = handler.createReply();
				reset();
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // getCount
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getCount());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // destroy
			{
			try
			{
				_out = handler.createReply();
				destroy();
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
		}
		return _out;
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
