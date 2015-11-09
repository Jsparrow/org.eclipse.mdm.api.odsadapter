package org.asam.ods;

/**
 * Generated from IDL union "TS_Union".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class TS_Union
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.asam.ods.DataType discriminator;
	private java.lang.String stringVal;
	private short shortVal;
	private float floatVal;
	private byte byteVal;
	private boolean booleanVal;
	private int longVal;
	private double doubleVal;
	private org.asam.ods.T_LONGLONG longlongVal;
	private org.asam.ods.T_COMPLEX complexVal;
	private org.asam.ods.T_DCOMPLEX dcomplexVal;
	private java.lang.String dateVal;
	private byte[] bytestrVal;
	private org.asam.ods.Blob blobVal;
	private java.lang.String[] stringSeq;
	private short[] shortSeq;
	private float[] floatSeq;
	private byte[] byteSeq;
	private boolean[] booleanSeq;
	private int[] longSeq;
	private double[] doubleSeq;
	private org.asam.ods.T_LONGLONG[] longlongSeq;
	private org.asam.ods.T_COMPLEX[] complexSeq;
	private org.asam.ods.T_DCOMPLEX[] dcomplexSeq;
	private java.lang.String[] dateSeq;
	private byte[][] bytestrSeq;
	private org.asam.ods.T_ExternalReference extRefVal;
	private org.asam.ods.T_ExternalReference[] extRefSeq;
	private int enumVal;
	private int[] enumSeq;

	public TS_Union ()
	{
	}

	public org.asam.ods.DataType discriminator ()
	{
		return discriminator;
	}

	public java.lang.String stringVal ()
	{
		if (discriminator != org.asam.ods.DataType.DT_STRING)
			throw new org.omg.CORBA.BAD_OPERATION();
		return stringVal;
	}

	public void stringVal (java.lang.String _x)
	{
		discriminator = org.asam.ods.DataType.DT_STRING;
		stringVal = _x;
	}

	public short shortVal ()
	{
		if (discriminator != org.asam.ods.DataType.DT_SHORT)
			throw new org.omg.CORBA.BAD_OPERATION();
		return shortVal;
	}

	public void shortVal (short _x)
	{
		discriminator = org.asam.ods.DataType.DT_SHORT;
		shortVal = _x;
	}

	public float floatVal ()
	{
		if (discriminator != org.asam.ods.DataType.DT_FLOAT)
			throw new org.omg.CORBA.BAD_OPERATION();
		return floatVal;
	}

	public void floatVal (float _x)
	{
		discriminator = org.asam.ods.DataType.DT_FLOAT;
		floatVal = _x;
	}

	public byte byteVal ()
	{
		if (discriminator != org.asam.ods.DataType.DT_BYTE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return byteVal;
	}

	public void byteVal (byte _x)
	{
		discriminator = org.asam.ods.DataType.DT_BYTE;
		byteVal = _x;
	}

	public boolean booleanVal ()
	{
		if (discriminator != org.asam.ods.DataType.DT_BOOLEAN)
			throw new org.omg.CORBA.BAD_OPERATION();
		return booleanVal;
	}

	public void booleanVal (boolean _x)
	{
		discriminator = org.asam.ods.DataType.DT_BOOLEAN;
		booleanVal = _x;
	}

	public int longVal ()
	{
		if (discriminator != org.asam.ods.DataType.DT_LONG)
			throw new org.omg.CORBA.BAD_OPERATION();
		return longVal;
	}

	public void longVal (int _x)
	{
		discriminator = org.asam.ods.DataType.DT_LONG;
		longVal = _x;
	}

	public double doubleVal ()
	{
		if (discriminator != org.asam.ods.DataType.DT_DOUBLE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return doubleVal;
	}

	public void doubleVal (double _x)
	{
		discriminator = org.asam.ods.DataType.DT_DOUBLE;
		doubleVal = _x;
	}

	public org.asam.ods.T_LONGLONG longlongVal ()
	{
		if (discriminator != org.asam.ods.DataType.DT_LONGLONG)
			throw new org.omg.CORBA.BAD_OPERATION();
		return longlongVal;
	}

	public void longlongVal (org.asam.ods.T_LONGLONG _x)
	{
		discriminator = org.asam.ods.DataType.DT_LONGLONG;
		longlongVal = _x;
	}

	public org.asam.ods.T_COMPLEX complexVal ()
	{
		if (discriminator != org.asam.ods.DataType.DT_COMPLEX)
			throw new org.omg.CORBA.BAD_OPERATION();
		return complexVal;
	}

	public void complexVal (org.asam.ods.T_COMPLEX _x)
	{
		discriminator = org.asam.ods.DataType.DT_COMPLEX;
		complexVal = _x;
	}

	public org.asam.ods.T_DCOMPLEX dcomplexVal ()
	{
		if (discriminator != org.asam.ods.DataType.DT_DCOMPLEX)
			throw new org.omg.CORBA.BAD_OPERATION();
		return dcomplexVal;
	}

	public void dcomplexVal (org.asam.ods.T_DCOMPLEX _x)
	{
		discriminator = org.asam.ods.DataType.DT_DCOMPLEX;
		dcomplexVal = _x;
	}

	public java.lang.String dateVal ()
	{
		if (discriminator != org.asam.ods.DataType.DT_DATE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return dateVal;
	}

	public void dateVal (java.lang.String _x)
	{
		discriminator = org.asam.ods.DataType.DT_DATE;
		dateVal = _x;
	}

	public byte[] bytestrVal ()
	{
		if (discriminator != org.asam.ods.DataType.DT_BYTESTR)
			throw new org.omg.CORBA.BAD_OPERATION();
		return bytestrVal;
	}

	public void bytestrVal (byte[] _x)
	{
		discriminator = org.asam.ods.DataType.DT_BYTESTR;
		bytestrVal = _x;
	}

	public org.asam.ods.Blob blobVal ()
	{
		if (discriminator != org.asam.ods.DataType.DT_BLOB)
			throw new org.omg.CORBA.BAD_OPERATION();
		return blobVal;
	}

	public void blobVal (org.asam.ods.Blob _x)
	{
		discriminator = org.asam.ods.DataType.DT_BLOB;
		blobVal = _x;
	}

	public java.lang.String[] stringSeq ()
	{
		if (discriminator != org.asam.ods.DataType.DS_STRING)
			throw new org.omg.CORBA.BAD_OPERATION();
		return stringSeq;
	}

	public void stringSeq (java.lang.String[] _x)
	{
		discriminator = org.asam.ods.DataType.DS_STRING;
		stringSeq = _x;
	}

	public short[] shortSeq ()
	{
		if (discriminator != org.asam.ods.DataType.DS_SHORT)
			throw new org.omg.CORBA.BAD_OPERATION();
		return shortSeq;
	}

	public void shortSeq (short[] _x)
	{
		discriminator = org.asam.ods.DataType.DS_SHORT;
		shortSeq = _x;
	}

	public float[] floatSeq ()
	{
		if (discriminator != org.asam.ods.DataType.DS_FLOAT)
			throw new org.omg.CORBA.BAD_OPERATION();
		return floatSeq;
	}

	public void floatSeq (float[] _x)
	{
		discriminator = org.asam.ods.DataType.DS_FLOAT;
		floatSeq = _x;
	}

	public byte[] byteSeq ()
	{
		if (discriminator != org.asam.ods.DataType.DS_BYTE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return byteSeq;
	}

	public void byteSeq (byte[] _x)
	{
		discriminator = org.asam.ods.DataType.DS_BYTE;
		byteSeq = _x;
	}

	public boolean[] booleanSeq ()
	{
		if (discriminator != org.asam.ods.DataType.DS_BOOLEAN)
			throw new org.omg.CORBA.BAD_OPERATION();
		return booleanSeq;
	}

	public void booleanSeq (boolean[] _x)
	{
		discriminator = org.asam.ods.DataType.DS_BOOLEAN;
		booleanSeq = _x;
	}

	public int[] longSeq ()
	{
		if (discriminator != org.asam.ods.DataType.DS_LONG)
			throw new org.omg.CORBA.BAD_OPERATION();
		return longSeq;
	}

	public void longSeq (int[] _x)
	{
		discriminator = org.asam.ods.DataType.DS_LONG;
		longSeq = _x;
	}

	public double[] doubleSeq ()
	{
		if (discriminator != org.asam.ods.DataType.DS_DOUBLE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return doubleSeq;
	}

	public void doubleSeq (double[] _x)
	{
		discriminator = org.asam.ods.DataType.DS_DOUBLE;
		doubleSeq = _x;
	}

	public org.asam.ods.T_LONGLONG[] longlongSeq ()
	{
		if (discriminator != org.asam.ods.DataType.DS_LONGLONG)
			throw new org.omg.CORBA.BAD_OPERATION();
		return longlongSeq;
	}

	public void longlongSeq (org.asam.ods.T_LONGLONG[] _x)
	{
		discriminator = org.asam.ods.DataType.DS_LONGLONG;
		longlongSeq = _x;
	}

	public org.asam.ods.T_COMPLEX[] complexSeq ()
	{
		if (discriminator != org.asam.ods.DataType.DS_COMPLEX)
			throw new org.omg.CORBA.BAD_OPERATION();
		return complexSeq;
	}

	public void complexSeq (org.asam.ods.T_COMPLEX[] _x)
	{
		discriminator = org.asam.ods.DataType.DS_COMPLEX;
		complexSeq = _x;
	}

	public org.asam.ods.T_DCOMPLEX[] dcomplexSeq ()
	{
		if (discriminator != org.asam.ods.DataType.DS_DCOMPLEX)
			throw new org.omg.CORBA.BAD_OPERATION();
		return dcomplexSeq;
	}

	public void dcomplexSeq (org.asam.ods.T_DCOMPLEX[] _x)
	{
		discriminator = org.asam.ods.DataType.DS_DCOMPLEX;
		dcomplexSeq = _x;
	}

	public java.lang.String[] dateSeq ()
	{
		if (discriminator != org.asam.ods.DataType.DS_DATE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return dateSeq;
	}

	public void dateSeq (java.lang.String[] _x)
	{
		discriminator = org.asam.ods.DataType.DS_DATE;
		dateSeq = _x;
	}

	public byte[][] bytestrSeq ()
	{
		if (discriminator != org.asam.ods.DataType.DS_BYTESTR)
			throw new org.omg.CORBA.BAD_OPERATION();
		return bytestrSeq;
	}

	public void bytestrSeq (byte[][] _x)
	{
		discriminator = org.asam.ods.DataType.DS_BYTESTR;
		bytestrSeq = _x;
	}

	public org.asam.ods.T_ExternalReference extRefVal ()
	{
		if (discriminator != org.asam.ods.DataType.DT_EXTERNALREFERENCE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return extRefVal;
	}

	public void extRefVal (org.asam.ods.T_ExternalReference _x)
	{
		discriminator = org.asam.ods.DataType.DT_EXTERNALREFERENCE;
		extRefVal = _x;
	}

	public org.asam.ods.T_ExternalReference[] extRefSeq ()
	{
		if (discriminator != org.asam.ods.DataType.DS_EXTERNALREFERENCE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return extRefSeq;
	}

	public void extRefSeq (org.asam.ods.T_ExternalReference[] _x)
	{
		discriminator = org.asam.ods.DataType.DS_EXTERNALREFERENCE;
		extRefSeq = _x;
	}

	public int enumVal ()
	{
		if (discriminator != org.asam.ods.DataType.DT_ENUM)
			throw new org.omg.CORBA.BAD_OPERATION();
		return enumVal;
	}

	public void enumVal (int _x)
	{
		discriminator = org.asam.ods.DataType.DT_ENUM;
		enumVal = _x;
	}

	public int[] enumSeq ()
	{
		if (discriminator != org.asam.ods.DataType.DS_ENUM)
			throw new org.omg.CORBA.BAD_OPERATION();
		return enumSeq;
	}

	public void enumSeq (int[] _x)
	{
		discriminator = org.asam.ods.DataType.DS_ENUM;
		enumSeq = _x;
	}

	public void __default ()
	{
		discriminator = org.asam.ods.DataType.DT_UNKNOWN;
	}
	public void __default (org.asam.ods.DataType _discriminator)
	{
		discriminator = _discriminator;
	}
}
