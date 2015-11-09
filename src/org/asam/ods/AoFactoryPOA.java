package org.asam.ods;


/**
 * Generated from IDL interface "AoFactory".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class AoFactoryPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.AoFactoryOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "newSessionNameValue", new java.lang.Integer(0));
		m_opsHash.put ( "newSession", new java.lang.Integer(1));
		m_opsHash.put ( "getInterfaceVersion", new java.lang.Integer(2));
		m_opsHash.put ( "getType", new java.lang.Integer(3));
		m_opsHash.put ( "getName", new java.lang.Integer(4));
		m_opsHash.put ( "getDescription", new java.lang.Integer(5));
	}
	private String[] ids = {"IDL:org/asam/ods/AoFactory:1.0"};
	public org.asam.ods.AoFactory _this()
	{
		return org.asam.ods.AoFactoryHelper.narrow(_this_object());
	}
	public org.asam.ods.AoFactory _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.AoFactoryHelper.narrow(_this_object(orb));
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
			case 0: // newSessionNameValue
			{
			try
			{
				org.asam.ods.NameValue[] _arg0=org.asam.ods.NameValueSequenceHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.AoSessionHelper.write(_out,newSessionNameValue(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // newSession
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.AoSessionHelper.write(_out,newSession(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // getInterfaceVersion
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getInterfaceVersion());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // getType
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getType());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // getName
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getName());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // getDescription
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getDescription());
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
