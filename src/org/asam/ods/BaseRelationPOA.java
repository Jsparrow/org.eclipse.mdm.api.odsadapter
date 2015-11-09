package org.asam.ods;


/**
 * Generated from IDL interface "BaseRelation".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class BaseRelationPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.BaseRelationOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "getRelationName", new java.lang.Integer(0));
		m_opsHash.put ( "getRelationship", new java.lang.Integer(1));
		m_opsHash.put ( "getRelationRange", new java.lang.Integer(2));
		m_opsHash.put ( "getInverseRelationRange", new java.lang.Integer(3));
		m_opsHash.put ( "getElem1", new java.lang.Integer(4));
		m_opsHash.put ( "getElem2", new java.lang.Integer(5));
		m_opsHash.put ( "getInverseRelationName", new java.lang.Integer(6));
		m_opsHash.put ( "getInverseRelationship", new java.lang.Integer(7));
		m_opsHash.put ( "getRelationType", new java.lang.Integer(8));
	}
	private String[] ids = {"IDL:org/asam/ods/BaseRelation:1.0"};
	public org.asam.ods.BaseRelation _this()
	{
		return org.asam.ods.BaseRelationHelper.narrow(_this_object());
	}
	public org.asam.ods.BaseRelation _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.BaseRelationHelper.narrow(_this_object(orb));
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
			case 0: // getRelationName
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getRelationName());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // getRelationship
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.RelationshipHelper.write(_out,getRelationship());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // getRelationRange
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.RelationRangeHelper.write(_out,getRelationRange());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // getInverseRelationRange
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.RelationRangeHelper.write(_out,getInverseRelationRange());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // getElem1
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.BaseElementHelper.write(_out,getElem1());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // getElem2
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.BaseElementHelper.write(_out,getElem2());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // getInverseRelationName
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getInverseRelationName());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 7: // getInverseRelationship
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.RelationshipHelper.write(_out,getInverseRelationship());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 8: // getRelationType
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.RelationTypeHelper.write(_out,getRelationType());
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
