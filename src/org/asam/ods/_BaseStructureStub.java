package org.asam.ods;


/**
 * Generated from IDL interface "BaseStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class _BaseStructureStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.asam.ods.BaseStructure
{
	private String[] ids = {"IDL:org/asam/ods/BaseStructure:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.asam.ods.BaseStructureOperations.class;
	public java.lang.String[] listTopLevelElements(java.lang.String bePattern) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listTopLevelElements", true);
				_os.write_string(bePattern);
				_is = _invoke(_os);
				java.lang.String[] _result = org.asam.ods.BaseTypeSequenceHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listTopLevelElements", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseStructureOperations _localServant = (BaseStructureOperations)_so.servant;
			java.lang.String[] _result;
			try
			{
				_result = _localServant.listTopLevelElements(bePattern);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.BaseElement[] getTopLevelElements(java.lang.String bePattern) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getTopLevelElements", true);
				_os.write_string(bePattern);
				_is = _invoke(_os);
				org.asam.ods.BaseElement[] _result = org.asam.ods.BaseElementSequenceHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getTopLevelElements", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseStructureOperations _localServant = (BaseStructureOperations)_so.servant;
			org.asam.ods.BaseElement[] _result;
			try
			{
				_result = _localServant.getTopLevelElements(bePattern);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.BaseElement[] getElements(java.lang.String bePattern) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getElements", true);
				_os.write_string(bePattern);
				_is = _invoke(_os);
				org.asam.ods.BaseElement[] _result = org.asam.ods.BaseElementSequenceHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getElements", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseStructureOperations _localServant = (BaseStructureOperations)_so.servant;
			org.asam.ods.BaseElement[] _result;
			try
			{
				_result = _localServant.getElements(bePattern);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.BaseElement getElementByType(java.lang.String beType) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getElementByType", true);
				_os.write_string(beType);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getElementByType", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseStructureOperations _localServant = (BaseStructureOperations)_so.servant;
			org.asam.ods.BaseElement _result;
			try
			{
				_result = _localServant.getElementByType(beType);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.BaseRelation[] getRelations(org.asam.ods.BaseElement elem1, org.asam.ods.BaseElement elem2) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getRelations", true);
				org.asam.ods.BaseElementHelper.write(_os,elem1);
				org.asam.ods.BaseElementHelper.write(_os,elem2);
				_is = _invoke(_os);
				org.asam.ods.BaseRelation[] _result = org.asam.ods.BaseRelationSequenceHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getRelations", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseStructureOperations _localServant = (BaseStructureOperations)_so.servant;
			org.asam.ods.BaseRelation[] _result;
			try
			{
				_result = _localServant.getRelations(elem1,elem2);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String[] listElements(java.lang.String bePattern) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listElements", true);
				_os.write_string(bePattern);
				_is = _invoke(_os);
				java.lang.String[] _result = org.asam.ods.BaseTypeSequenceHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listElements", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseStructureOperations _localServant = (BaseStructureOperations)_so.servant;
			java.lang.String[] _result;
			try
			{
				_result = _localServant.listElements(bePattern);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String getVersion() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getVersion", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getVersion", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseStructureOperations _localServant = (BaseStructureOperations)_so.servant;
			java.lang.String _result;
			try
			{
				_result = _localServant.getVersion();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.BaseRelation getRelation(org.asam.ods.BaseElement elem1, org.asam.ods.BaseElement elem2) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getRelation", true);
				org.asam.ods.BaseElementHelper.write(_os,elem1);
				org.asam.ods.BaseElementHelper.write(_os,elem2);
				_is = _invoke(_os);
				org.asam.ods.BaseRelation _result = org.asam.ods.BaseRelationHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getRelation", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseStructureOperations _localServant = (BaseStructureOperations)_so.servant;
			org.asam.ods.BaseRelation _result;
			try
			{
				_result = _localServant.getRelation(elem1,elem2);
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
