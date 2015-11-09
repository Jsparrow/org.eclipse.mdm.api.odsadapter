package org.asam.ods;


/**
 * Generated from IDL interface "BaseElement".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class BaseElementPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.BaseElementOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "getAttributes", new java.lang.Integer(0));
		m_opsHash.put ( "isTopLevel", new java.lang.Integer(1));
		m_opsHash.put ( "getRelatedElementsByRelationship", new java.lang.Integer(2));
		m_opsHash.put ( "listRelatedElementsByRelationship", new java.lang.Integer(3));
		m_opsHash.put ( "getRelationsByType", new java.lang.Integer(4));
		m_opsHash.put ( "getType", new java.lang.Integer(5));
		m_opsHash.put ( "listAttributes", new java.lang.Integer(6));
		m_opsHash.put ( "getAllRelations", new java.lang.Integer(7));
	}
	private String[] ids = {"IDL:org/asam/ods/BaseElement:1.0"};
	public org.asam.ods.BaseElement _this()
	{
		return org.asam.ods.BaseElementHelper.narrow(_this_object());
	}
	public org.asam.ods.BaseElement _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.BaseElementHelper.narrow(_this_object(orb));
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
			case 0: // getAttributes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.BaseAttributeSequenceHelper.write(_out,getAttributes(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // isTopLevel
			{
			try
			{
				_out = handler.createReply();
				_out.write_boolean(isTopLevel());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // getRelatedElementsByRelationship
			{
			try
			{
				org.asam.ods.Relationship _arg0=org.asam.ods.RelationshipHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.BaseElementSequenceHelper.write(_out,getRelatedElementsByRelationship(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // listRelatedElementsByRelationship
			{
			try
			{
				org.asam.ods.Relationship _arg0=org.asam.ods.RelationshipHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.BaseTypeSequenceHelper.write(_out,listRelatedElementsByRelationship(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // getRelationsByType
			{
			try
			{
				org.asam.ods.RelationType _arg0=org.asam.ods.RelationTypeHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.BaseRelationSequenceHelper.write(_out,getRelationsByType(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // getType
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
			case 6: // listAttributes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.NameSequenceHelper.write(_out,listAttributes(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 7: // getAllRelations
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.BaseRelationSequenceHelper.write(_out,getAllRelations());
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
