package org.asam.ods;


/**
 * Generated from IDL interface "BaseRelation".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class _BaseRelationStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.asam.ods.BaseRelation
{
	private String[] ids = {"IDL:org/asam/ods/BaseRelation:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.asam.ods.BaseRelationOperations.class;
	public java.lang.String getRelationName() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getRelationName", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getRelationName", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseRelationOperations _localServant = (BaseRelationOperations)_so.servant;
			java.lang.String _result;
			try
			{
				_result = _localServant.getRelationName();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.Relationship getRelationship() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getRelationship", true);
				_is = _invoke(_os);
				org.asam.ods.Relationship _result = org.asam.ods.RelationshipHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getRelationship", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseRelationOperations _localServant = (BaseRelationOperations)_so.servant;
			org.asam.ods.Relationship _result;
			try
			{
				_result = _localServant.getRelationship();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.RelationRange getRelationRange() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getRelationRange", true);
				_is = _invoke(_os);
				org.asam.ods.RelationRange _result = org.asam.ods.RelationRangeHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getRelationRange", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseRelationOperations _localServant = (BaseRelationOperations)_so.servant;
			org.asam.ods.RelationRange _result;
			try
			{
				_result = _localServant.getRelationRange();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.RelationRange getInverseRelationRange() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInverseRelationRange", true);
				_is = _invoke(_os);
				org.asam.ods.RelationRange _result = org.asam.ods.RelationRangeHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getInverseRelationRange", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseRelationOperations _localServant = (BaseRelationOperations)_so.servant;
			org.asam.ods.RelationRange _result;
			try
			{
				_result = _localServant.getInverseRelationRange();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.BaseElement getElem1() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getElem1", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getElem1", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseRelationOperations _localServant = (BaseRelationOperations)_so.servant;
			org.asam.ods.BaseElement _result;
			try
			{
				_result = _localServant.getElem1();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.BaseElement getElem2() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getElem2", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getElem2", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseRelationOperations _localServant = (BaseRelationOperations)_so.servant;
			org.asam.ods.BaseElement _result;
			try
			{
				_result = _localServant.getElem2();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String getInverseRelationName() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInverseRelationName", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getInverseRelationName", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseRelationOperations _localServant = (BaseRelationOperations)_so.servant;
			java.lang.String _result;
			try
			{
				_result = _localServant.getInverseRelationName();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.Relationship getInverseRelationship() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInverseRelationship", true);
				_is = _invoke(_os);
				org.asam.ods.Relationship _result = org.asam.ods.RelationshipHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getInverseRelationship", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseRelationOperations _localServant = (BaseRelationOperations)_so.servant;
			org.asam.ods.Relationship _result;
			try
			{
				_result = _localServant.getInverseRelationship();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.RelationType getRelationType() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getRelationType", true);
				_is = _invoke(_os);
				org.asam.ods.RelationType _result = org.asam.ods.RelationTypeHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getRelationType", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseRelationOperations _localServant = (BaseRelationOperations)_so.servant;
			org.asam.ods.RelationType _result;
			try
			{
				_result = _localServant.getRelationType();
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
