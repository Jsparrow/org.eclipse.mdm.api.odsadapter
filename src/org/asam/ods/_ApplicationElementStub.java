package org.asam.ods;


/**
 * Generated from IDL interface "ApplicationElement".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class _ApplicationElementStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.asam.ods.ApplicationElement
{
	private String[] ids = {"IDL:org/asam/ods/ApplicationElement:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.asam.ods.ApplicationElementOperations.class;
	public java.lang.String[] listAttributes(java.lang.String aaPattern) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listAttributes", true);
				_os.write_string(aaPattern);
				_is = _invoke(_os);
				java.lang.String[] _result = org.asam.ods.NameSequenceHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listAttributes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			java.lang.String[] _result;
			try
			{
				_result = _localServant.listAttributes(aaPattern);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setRights(org.asam.ods.InstanceElement usergroup, int rights, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setRights", true);
				org.asam.ods.InstanceElementHelper.write(_os,usergroup);
				_os.write_long(rights);
				org.asam.ods.RightsSetHelper.write(_os,set);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setRights", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			try
			{
				_localServant.setRights(usergroup,rights,set);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public java.lang.String getName() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getName", true);
				_is = _invoke(_os);
				java.lang.String _result = _is.read_string();
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getName", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			java.lang.String _result;
			try
			{
				_result = _localServant.getName();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.InitialRight[] getInitialRights() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInitialRights", true);
				_is = _invoke(_os);
				org.asam.ods.InitialRight[] _result = org.asam.ods.InitialRightSequenceHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getInitialRights", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.InitialRight[] _result;
			try
			{
				_result = _localServant.getInitialRights();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void removeAttribute(org.asam.ods.ApplicationAttribute applAttr) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "removeAttribute", true);
				org.asam.ods.ApplicationAttributeHelper.write(_os,applAttr);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "removeAttribute", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			try
			{
				_localServant.removeAttribute(applAttr);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public int getSecurityLevel() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getSecurityLevel", true);
				_is = _invoke(_os);
				int _result = _is.read_long();
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getSecurityLevel", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			int _result;
			try
			{
				_result = _localServant.getSecurityLevel();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.ApplicationAttribute createAttribute() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createAttribute", true);
				_is = _invoke(_os);
				org.asam.ods.ApplicationAttribute _result = org.asam.ods.ApplicationAttributeHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createAttribute", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.ApplicationAttribute _result;
			try
			{
				_result = _localServant.createAttribute();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.T_LONGLONG getId() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getId", true);
				_is = _invoke(_os);
				org.asam.ods.T_LONGLONG _result = org.asam.ods.T_LONGLONGHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getId", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.T_LONGLONG _result;
			try
			{
				_result = _localServant.getId();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.ApplicationRelation[] getRelationsByBaseName(java.lang.String baseRelName) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getRelationsByBaseName", true);
				_os.write_string(baseRelName);
				_is = _invoke(_os);
				org.asam.ods.ApplicationRelation[] _result = org.asam.ods.ApplicationRelationSequenceHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getRelationsByBaseName", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.ApplicationRelation[] _result;
			try
			{
				_result = _localServant.getRelationsByBaseName(baseRelName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.InstanceElement[] createInstances(org.asam.ods.NameValueSeqUnit[] attributes, org.asam.ods.ApplicationRelationInstanceElementSeq[] relatedInstances) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createInstances", true);
				org.asam.ods.NameValueSeqUnitSequenceHelper.write(_os,attributes);
				org.asam.ods.ApplicationRelationInstanceElementSeqSequenceHelper.write(_os,relatedInstances);
				_is = _invoke(_os);
				org.asam.ods.InstanceElement[] _result = org.asam.ods.InstanceElementSequenceHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createInstances", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.InstanceElement[] _result;
			try
			{
				_result = _localServant.createInstances(attributes,relatedInstances);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setSecurityLevel(int secLevel, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setSecurityLevel", true);
				_os.write_long(secLevel);
				org.asam.ods.RightsSetHelper.write(_os,set);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setSecurityLevel", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			try
			{
				_localServant.setSecurityLevel(secLevel,set);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.asam.ods.ApplicationRelation[] getInitialRightRelations() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInitialRightRelations", true);
				_is = _invoke(_os);
				org.asam.ods.ApplicationRelation[] _result = org.asam.ods.ApplicationRelationSequenceHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getInitialRightRelations", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.ApplicationRelation[] _result;
			try
			{
				_result = _localServant.getInitialRightRelations();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String[] listRelatedElementsByRelationship(org.asam.ods.Relationship aeRelationship) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listRelatedElementsByRelationship", true);
				org.asam.ods.RelationshipHelper.write(_os,aeRelationship);
				_is = _invoke(_os);
				java.lang.String[] _result = org.asam.ods.NameSequenceHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listRelatedElementsByRelationship", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			java.lang.String[] _result;
			try
			{
				_result = _localServant.listRelatedElementsByRelationship(aeRelationship);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setBaseElement(org.asam.ods.BaseElement baseElem) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setBaseElement", true);
				org.asam.ods.BaseElementHelper.write(_os,baseElem);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setBaseElement", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			try
			{
				_localServant.setBaseElement(baseElem);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.asam.ods.ApplicationAttribute getAttributeByBaseName(java.lang.String baName) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getAttributeByBaseName", true);
				_os.write_string(baName);
				_is = _invoke(_os);
				org.asam.ods.ApplicationAttribute _result = org.asam.ods.ApplicationAttributeHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getAttributeByBaseName", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.ApplicationAttribute _result;
			try
			{
				_result = _localServant.getAttributeByBaseName(baName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.InstanceElement getInstanceById(org.asam.ods.T_LONGLONG ieId) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInstanceById", true);
				org.asam.ods.T_LONGLONGHelper.write(_os,ieId);
				_is = _invoke(_os);
				org.asam.ods.InstanceElement _result = org.asam.ods.InstanceElementHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getInstanceById", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.InstanceElement _result;
			try
			{
				_result = _localServant.getInstanceById(ieId);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.NameIterator listInstances(java.lang.String aaPattern) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listInstances", true);
				_os.write_string(aaPattern);
				_is = _invoke(_os);
				org.asam.ods.NameIterator _result = org.asam.ods.NameIteratorHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listInstances", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.NameIterator _result;
			try
			{
				_result = _localServant.listInstances(aaPattern);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.ApplicationElement[] getAllRelatedElements() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getAllRelatedElements", true);
				_is = _invoke(_os);
				org.asam.ods.ApplicationElement[] _result = org.asam.ods.ApplicationElementSequenceHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getAllRelatedElements", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.ApplicationElement[] _result;
			try
			{
				_result = _localServant.getAllRelatedElements();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String[] listAllRelatedElements() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listAllRelatedElements", true);
				_is = _invoke(_os);
				java.lang.String[] _result = org.asam.ods.NameSequenceHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listAllRelatedElements", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			java.lang.String[] _result;
			try
			{
				_result = _localServant.listAllRelatedElements();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.ApplicationElement[] getRelatedElementsByRelationship(org.asam.ods.Relationship aeRelationship) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getRelatedElementsByRelationship", true);
				org.asam.ods.RelationshipHelper.write(_os,aeRelationship);
				_is = _invoke(_os);
				org.asam.ods.ApplicationElement[] _result = org.asam.ods.ApplicationElementSequenceHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getRelatedElementsByRelationship", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.ApplicationElement[] _result;
			try
			{
				_result = _localServant.getRelatedElementsByRelationship(aeRelationship);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.BaseElement getBaseElement() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getBaseElement", true);
				_is = _invoke(_os);
				org.asam.ods.BaseElement _result = org.asam.ods.BaseElementHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getBaseElement", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.BaseElement _result;
			try
			{
				_result = _localServant.getBaseElement();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.ACL[] getRights() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getRights", true);
				_is = _invoke(_os);
				org.asam.ods.ACL[] _result = org.asam.ods.ACLSequenceHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getRights", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.ACL[] _result;
			try
			{
				_result = _localServant.getRights();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setName(java.lang.String aeName) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setName", true);
				_os.write_string(aeName);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setName", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			try
			{
				_localServant.setName(aeName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.asam.ods.ApplicationStructure getApplicationStructure() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getApplicationStructure", true);
				_is = _invoke(_os);
				org.asam.ods.ApplicationStructure _result = org.asam.ods.ApplicationStructureHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getApplicationStructure", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.ApplicationStructure _result;
			try
			{
				_result = _localServant.getApplicationStructure();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setInitialRights(org.asam.ods.InstanceElement usergroup, int rights, org.asam.ods.T_LONGLONG refAid, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setInitialRights", true);
				org.asam.ods.InstanceElementHelper.write(_os,usergroup);
				_os.write_long(rights);
				org.asam.ods.T_LONGLONGHelper.write(_os,refAid);
				org.asam.ods.RightsSetHelper.write(_os,set);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setInitialRights", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			try
			{
				_localServant.setInitialRights(usergroup,rights,refAid,set);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.asam.ods.ApplicationAttribute[] getAttributes(java.lang.String aaPattern) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getAttributes", true);
				_os.write_string(aaPattern);
				_is = _invoke(_os);
				org.asam.ods.ApplicationAttribute[] _result = org.asam.ods.ApplicationAttributeSequenceHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getAttributes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.ApplicationAttribute[] _result;
			try
			{
				_result = _localServant.getAttributes(aaPattern);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.ApplicationRelation[] getAllRelations() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getAllRelations", true);
				_is = _invoke(_os);
				org.asam.ods.ApplicationRelation[] _result = org.asam.ods.ApplicationRelationSequenceHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getAllRelations", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.ApplicationRelation[] _result;
			try
			{
				_result = _localServant.getAllRelations();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.InstanceElementIterator getInstances(java.lang.String iePattern) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInstances", true);
				_os.write_string(iePattern);
				_is = _invoke(_os);
				org.asam.ods.InstanceElementIterator _result = org.asam.ods.InstanceElementIteratorHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getInstances", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.InstanceElementIterator _result;
			try
			{
				_result = _localServant.getInstances(iePattern);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.InstanceElement getInstanceByName(java.lang.String ieName) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInstanceByName", true);
				_os.write_string(ieName);
				_is = _invoke(_os);
				org.asam.ods.InstanceElement _result = org.asam.ods.InstanceElementHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getInstanceByName", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.InstanceElement _result;
			try
			{
				_result = _localServant.getInstanceByName(ieName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.InstanceElement createInstance(java.lang.String ieName) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createInstance", true);
				_os.write_string(ieName);
				_is = _invoke(_os);
				org.asam.ods.InstanceElement _result = org.asam.ods.InstanceElementHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createInstance", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.InstanceElement _result;
			try
			{
				_result = _localServant.createInstance(ieName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setInitialRightRelation(org.asam.ods.ApplicationRelation applRel, boolean set) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setInitialRightRelation", true);
				org.asam.ods.ApplicationRelationHelper.write(_os,applRel);
				_os.write_boolean(set);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setInitialRightRelation", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			try
			{
				_localServant.setInitialRightRelation(applRel,set);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.asam.ods.ApplicationRelation[] getRelationsByType(org.asam.ods.RelationType aeRelationType) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getRelationsByType", true);
				org.asam.ods.RelationTypeHelper.write(_os,aeRelationType);
				_is = _invoke(_os);
				org.asam.ods.ApplicationRelation[] _result = org.asam.ods.ApplicationRelationSequenceHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getRelationsByType", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.ApplicationRelation[] _result;
			try
			{
				_result = _localServant.getRelationsByType(aeRelationType);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.ApplicationAttribute getAttributeByName(java.lang.String aaName) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getAttributeByName", true);
				_os.write_string(aaName);
				_is = _invoke(_os);
				org.asam.ods.ApplicationAttribute _result = org.asam.ods.ApplicationAttributeHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getAttributeByName", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			org.asam.ods.ApplicationAttribute _result;
			try
			{
				_result = _localServant.getAttributeByName(aaName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void removeInstance(org.asam.ods.T_LONGLONG ieId, boolean recursive) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "removeInstance", true);
				org.asam.ods.T_LONGLONGHelper.write(_os,ieId);
				_os.write_boolean(recursive);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "removeInstance", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplicationElementOperations _localServant = (ApplicationElementOperations)_so.servant;
			try
			{
				_localServant.removeInstance(ieId,recursive);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

}
