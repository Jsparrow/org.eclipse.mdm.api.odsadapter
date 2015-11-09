package org.asam.ods;


/**
 * Generated from IDL interface "Blob".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class BlobPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.BlobOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setHeader", new java.lang.Integer(0));
		m_opsHash.put ( "getLength", new java.lang.Integer(1));
		m_opsHash.put ( "get", new java.lang.Integer(2));
		m_opsHash.put ( "getHeader", new java.lang.Integer(3));
		m_opsHash.put ( "append", new java.lang.Integer(4));
		m_opsHash.put ( "compare", new java.lang.Integer(5));
		m_opsHash.put ( "destroy", new java.lang.Integer(6));
		m_opsHash.put ( "set", new java.lang.Integer(7));
	}
	private String[] ids = {"IDL:org/asam/ods/Blob:1.0"};
	public org.asam.ods.Blob _this()
	{
		return org.asam.ods.BlobHelper.narrow(_this_object());
	}
	public org.asam.ods.Blob _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.BlobHelper.narrow(_this_object(orb));
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
			case 0: // setHeader
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				setHeader(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // getLength
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getLength());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // get
			{
			try
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				_out = handler.createReply();
				org.asam.ods.S_BYTEHelper.write(_out,get(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // getHeader
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getHeader());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // append
			{
			try
			{
				byte[] _arg0=org.asam.ods.S_BYTEHelper.read(_input);
				_out = handler.createReply();
				append(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // compare
			{
			try
			{
				org.asam.ods.Blob _arg0=org.asam.ods.BlobHelper.read(_input);
				_out = handler.createReply();
				_out.write_boolean(compare(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // destroy
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
			case 7: // set
			{
			try
			{
				byte[] _arg0=org.asam.ods.S_BYTEHelper.read(_input);
				_out = handler.createReply();
				set(_arg0);
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
