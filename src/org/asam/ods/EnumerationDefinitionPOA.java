package org.asam.ods;


/**
 * Generated from IDL interface "EnumerationDefinition".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class EnumerationDefinitionPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.EnumerationDefinitionOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "getIndex", new java.lang.Integer(0));
		m_opsHash.put ( "setName", new java.lang.Integer(1));
		m_opsHash.put ( "getItemName", new java.lang.Integer(2));
		m_opsHash.put ( "renameItem", new java.lang.Integer(3));
		m_opsHash.put ( "listItemNames", new java.lang.Integer(4));
		m_opsHash.put ( "getItem", new java.lang.Integer(5));
		m_opsHash.put ( "addItem", new java.lang.Integer(6));
		m_opsHash.put ( "getName", new java.lang.Integer(7));
	}
	private String[] ids = {"IDL:org/asam/ods/EnumerationDefinition:1.0"};
	public org.asam.ods.EnumerationDefinition _this()
	{
		return org.asam.ods.EnumerationDefinitionHelper.narrow(_this_object());
	}
	public org.asam.ods.EnumerationDefinition _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.EnumerationDefinitionHelper.narrow(_this_object(orb));
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
			case 0: // getIndex
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getIndex());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // setName
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				setName(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // getItemName
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				_out.write_string(getItemName(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // renameItem
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				renameItem(_arg0,_arg1);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // listItemNames
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.NameSequenceHelper.write(_out,listItemNames());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // getItem
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				_out.write_long(getItem(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // addItem
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				addItem(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 7: // getName
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
		}
		return _out;
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
