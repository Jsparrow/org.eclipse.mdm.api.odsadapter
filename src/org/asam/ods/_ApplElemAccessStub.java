package org.asam.ods;


/**
 * Generated from IDL interface "ApplElemAccess".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class _ApplElemAccessStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.asam.ods.ApplElemAccess
{
	private String[] ids = {"IDL:org/asam/ods/ApplElemAccess:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.asam.ods.ApplElemAccessOperations.class;
	public void updateInstances(org.asam.ods.AIDNameValueSeqUnitId[] val) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "updateInstances", true);
				org.asam.ods.AIDNameValueSeqUnitIdSequenceHelper.write(_os,val);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "updateInstances", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			try
			{
				_localServant.updateInstances(val);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.asam.ods.ElemId[] insertInstances(org.asam.ods.AIDNameValueSeqUnitId[] val) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "insertInstances", true);
				org.asam.ods.AIDNameValueSeqUnitIdSequenceHelper.write(_os,val);
				_is = _invoke(_os);
				org.asam.ods.ElemId[] _result = org.asam.ods.ElemIdSequenceHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "insertInstances", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			org.asam.ods.ElemId[] _result;
			try
			{
				_result = _localServant.insertInstances(val);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setElementRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG usergroupId, int rights, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setElementRights", true);
				org.asam.ods.T_LONGLONGHelper.write(_os,aid);
				org.asam.ods.T_LONGLONGHelper.write(_os,usergroupId);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setElementRights", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			try
			{
				_localServant.setElementRights(aid,usergroupId,rights,set);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.asam.ods.ACL[] getAttributeRights(org.asam.ods.T_LONGLONG aid, java.lang.String attrName) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getAttributeRights", true);
				org.asam.ods.T_LONGLONGHelper.write(_os,aid);
				_os.write_string(attrName);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getAttributeRights", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			org.asam.ods.ACL[] _result;
			try
			{
				_result = _localServant.getAttributeRights(aid,attrName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String[] getInitialRightReference(org.asam.ods.T_LONGLONG aid) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInitialRightReference", true);
				org.asam.ods.T_LONGLONGHelper.write(_os,aid);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getInitialRightReference", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			java.lang.String[] _result;
			try
			{
				_result = _localServant.getInitialRightReference(aid);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setInitialRightReference(org.asam.ods.T_LONGLONG aid, java.lang.String refName, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setInitialRightReference", true);
				org.asam.ods.T_LONGLONGHelper.write(_os,aid);
				_os.write_string(refName);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setInitialRightReference", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			try
			{
				_localServant.setInitialRightReference(aid,refName,set);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void setInstanceInitialRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG[] instIds, org.asam.ods.T_LONGLONG usergroupId, int rights, org.asam.ods.T_LONGLONG refAid, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setInstanceInitialRights", true);
				org.asam.ods.T_LONGLONGHelper.write(_os,aid);
				org.asam.ods.S_LONGLONGHelper.write(_os,instIds);
				org.asam.ods.T_LONGLONGHelper.write(_os,usergroupId);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setInstanceInitialRights", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			try
			{
				_localServant.setInstanceInitialRights(aid,instIds,usergroupId,rights,refAid,set);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.asam.ods.InitialRight[] getElementInitialRights(org.asam.ods.T_LONGLONG aid) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getElementInitialRights", true);
				org.asam.ods.T_LONGLONGHelper.write(_os,aid);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getElementInitialRights", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			org.asam.ods.InitialRight[] _result;
			try
			{
				_result = _localServant.getElementInitialRights(aid);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.ElemResultSet[] getInstances(org.asam.ods.QueryStructure aoq, int how_many) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInstances", true);
				org.asam.ods.QueryStructureHelper.write(_os,aoq);
				_os.write_long(how_many);
				_is = _invoke(_os);
				org.asam.ods.ElemResultSet[] _result = org.asam.ods.ElemResultSetSequenceHelper.read(_is);
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
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			org.asam.ods.ElemResultSet[] _result;
			try
			{
				_result = _localServant.getInstances(aoq,how_many);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setElementInitialRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG usergroupId, int rights, org.asam.ods.T_LONGLONG refAid, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setElementInitialRights", true);
				org.asam.ods.T_LONGLONGHelper.write(_os,aid);
				org.asam.ods.T_LONGLONGHelper.write(_os,usergroupId);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setElementInitialRights", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			try
			{
				_localServant.setElementInitialRights(aid,usergroupId,rights,refAid,set);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.asam.ods.ACL[] getElementRights(org.asam.ods.T_LONGLONG aid) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getElementRights", true);
				org.asam.ods.T_LONGLONGHelper.write(_os,aid);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getElementRights", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			org.asam.ods.ACL[] _result;
			try
			{
				_result = _localServant.getElementRights(aid);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setInstanceRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG[] instIds, org.asam.ods.T_LONGLONG usergroupId, int rights, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setInstanceRights", true);
				org.asam.ods.T_LONGLONGHelper.write(_os,aid);
				org.asam.ods.S_LONGLONGHelper.write(_os,instIds);
				org.asam.ods.T_LONGLONGHelper.write(_os,usergroupId);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setInstanceRights", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			try
			{
				_localServant.setInstanceRights(aid,instIds,usergroupId,rights,set);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.asam.ods.InitialRight[] getInstanceInitialRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG iid) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInstanceInitialRights", true);
				org.asam.ods.T_LONGLONGHelper.write(_os,aid);
				org.asam.ods.T_LONGLONGHelper.write(_os,iid);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getInstanceInitialRights", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			org.asam.ods.InitialRight[] _result;
			try
			{
				_result = _localServant.getInstanceInitialRights(aid,iid);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.ValueMatrix getValueMatrix(org.asam.ods.ElemId elem) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getValueMatrix", true);
				org.asam.ods.ElemIdHelper.write(_os,elem);
				_is = _invoke(_os);
				org.asam.ods.ValueMatrix _result = org.asam.ods.ValueMatrixHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getValueMatrix", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			org.asam.ods.ValueMatrix _result;
			try
			{
				_result = _localServant.getValueMatrix(elem);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setAttributeRights(org.asam.ods.T_LONGLONG aid, java.lang.String attrName, org.asam.ods.T_LONGLONG usergroupId, int rights, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setAttributeRights", true);
				org.asam.ods.T_LONGLONGHelper.write(_os,aid);
				_os.write_string(attrName);
				org.asam.ods.T_LONGLONGHelper.write(_os,usergroupId);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setAttributeRights", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			try
			{
				_localServant.setAttributeRights(aid,attrName,usergroupId,rights,set);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.asam.ods.ACL[] getInstanceRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG iid) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInstanceRights", true);
				org.asam.ods.T_LONGLONGHelper.write(_os,aid);
				org.asam.ods.T_LONGLONGHelper.write(_os,iid);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getInstanceRights", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			org.asam.ods.ACL[] _result;
			try
			{
				_result = _localServant.getInstanceRights(aid,iid);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.ResultSetExt[] getInstancesExt(org.asam.ods.QueryStructureExt aoq, int how_many) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInstancesExt", true);
				org.asam.ods.QueryStructureExtHelper.write(_os,aoq);
				_os.write_long(how_many);
				_is = _invoke(_os);
				org.asam.ods.ResultSetExt[] _result = org.asam.ods.ResultSetExtSequenceHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getInstancesExt", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			org.asam.ods.ResultSetExt[] _result;
			try
			{
				_result = _localServant.getInstancesExt(aoq,how_many);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void deleteInstances(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG[] instIds) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "deleteInstances", true);
				org.asam.ods.T_LONGLONGHelper.write(_os,aid);
				org.asam.ods.S_LONGLONGHelper.write(_os,instIds);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "deleteInstances", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			try
			{
				_localServant.deleteInstances(aid,instIds);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.asam.ods.ValueMatrix getValueMatrixInMode(org.asam.ods.ElemId elem, org.asam.ods.ValueMatrixMode vmMode) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getValueMatrixInMode", true);
				org.asam.ods.ElemIdHelper.write(_os,elem);
				org.asam.ods.ValueMatrixModeHelper.write(_os,vmMode);
				_is = _invoke(_os);
				org.asam.ods.ValueMatrix _result = org.asam.ods.ValueMatrixHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getValueMatrixInMode", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			org.asam.ods.ValueMatrix _result;
			try
			{
				_result = _localServant.getValueMatrixInMode(elem,vmMode);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.ODSFile getODSFile(org.asam.ods.ElemId elem) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getODSFile", true);
				org.asam.ods.ElemIdHelper.write(_os,elem);
				_is = _invoke(_os);
				org.asam.ods.ODSFile _result = org.asam.ods.ODSFileHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getODSFile", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			org.asam.ods.ODSFile _result;
			try
			{
				_result = _localServant.getODSFile(elem);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setRelInst(org.asam.ods.ElemId elem, java.lang.String relName, org.asam.ods.T_LONGLONG[] instIds, org.asam.ods.SetType type) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setRelInst", true);
				org.asam.ods.ElemIdHelper.write(_os,elem);
				_os.write_string(relName);
				org.asam.ods.S_LONGLONGHelper.write(_os,instIds);
				org.asam.ods.SetTypeHelper.write(_os,type);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setRelInst", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			try
			{
				_localServant.setRelInst(elem,relName,instIds,type);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.asam.ods.T_LONGLONG[] getRelInst(org.asam.ods.ElemId elem, java.lang.String relName) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getRelInst", true);
				org.asam.ods.ElemIdHelper.write(_os,elem);
				_os.write_string(relName);
				_is = _invoke(_os);
				org.asam.ods.T_LONGLONG[] _result = org.asam.ods.S_LONGLONGHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getRelInst", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			ApplElemAccessOperations _localServant = (ApplElemAccessOperations)_so.servant;
			org.asam.ods.T_LONGLONG[] _result;
			try
			{
				_result = _localServant.getRelInst(elem,relName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

}
