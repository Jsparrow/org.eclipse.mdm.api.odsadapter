/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.mdm.api.base.model.AxisType;
import org.eclipse.mdm.api.base.model.EnumRegistry;
import org.eclipse.mdm.api.base.model.EnumerationValue;
import org.eclipse.mdm.api.base.model.Enumeration;
import org.eclipse.mdm.api.base.model.Interpolation;
import org.eclipse.mdm.api.base.model.ScalarType;
import org.eclipse.mdm.api.base.model.SequenceRepresentation;
import org.eclipse.mdm.api.base.model.TypeSpecification;
import org.eclipse.mdm.api.base.model.VersionState;

/**
 * Utility class for enumeration constant conversions from/to ODS types.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public final class ODSEnumerations {

	// ======================================================================
	// Class variables
	// ======================================================================

	private static final String SCALAR_TYPE_NAME = "datatype_enum";
	private static final String STATE_NAME = "valid_enum";
	private static final String INTERPOLATION_NAME = "interpolation_enum";
	private static final String AXIS_TYPE_NAME = "axistype";
	private static final String TYPE_SPECIFICATION_NAME = "typespec_enum";
	private static final String SEQUENCE_REPRESENTATION_NAME = "seq_rep_enum";

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 */
	private ODSEnumerations() {
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Returns the enumeration class identified by given name.
	 *
	 * @param <E>
	 *            The enumeration type.
	 * @param name
	 *            The ODS name of the requested enumeration class.
	 * @return The corresponding enumeration class is returned.
	 * @throws IllegalArgumentException
	 *             Thrown if ODS enumeration name is unknown.
	 */
	// FIXME (Florian Schmitt): can we do this simpler?
	public static Enumeration<? extends EnumerationValue> getEnumObj(String name) {
		EnumRegistry er=EnumRegistry.getInstance();
		if (SCALAR_TYPE_NAME.equals(name)) {
			return (er.get(EnumRegistry.SCALAR_TYPE));
		} else if (STATE_NAME.equals(name)) {
			return (er.get(EnumRegistry.VERSION_STATE));
		} else if (INTERPOLATION_NAME.equals(name)) {
			return (er.get(EnumRegistry.INTERPOLATION));
		} else if (AXIS_TYPE_NAME.equals(name)) {
			return (er.get(EnumRegistry.AXIS_TYPE));
		} else if (TYPE_SPECIFICATION_NAME.equals(name)) {
			return (er.get(EnumRegistry.TYPE_SPECIFICATION));
		} else if (SEQUENCE_REPRESENTATION_NAME.equals(name)) {
			return (er.get(EnumRegistry.SEQUENCE_REPRESENTATION));
		} else {
		   return er.get(name);
		}
	}

	/**
	 * Returns the ODS enumeration name for given enumeration class.
	 *
	 * @param enumObj
	 *            The enumeration object.
	 * @return The corresponding ODS enumeration name is returned.
	 * @throws IllegalArgumentException
	 *             Thrown if enumeration class is unknown.
	 */
	public static String getEnumName(Enumeration<?> enumObj) {
		if (enumObj == null) {
			throw new IllegalArgumentException("EnumerationValue class is not allowed to be null.");
		} else if (EnumRegistry.SCALAR_TYPE.equals(enumObj.getName())) {
			return SCALAR_TYPE_NAME;
		} else if (EnumRegistry.VERSION_STATE.equals(enumObj.getName())) {
			return STATE_NAME;
		} else if (EnumRegistry.INTERPOLATION.equals(enumObj.getName())) {
			return INTERPOLATION_NAME;
		} else if (EnumRegistry.AXIS_TYPE.equals(enumObj.getName())) {
			return AXIS_TYPE_NAME;
		} else if (EnumRegistry.TYPE_SPECIFICATION.equals(enumObj.getName())) {
			return TYPE_SPECIFICATION_NAME;
		} else if (EnumRegistry.SEQUENCE_REPRESENTATION.equals(enumObj.getName())) {
			return SEQUENCE_REPRESENTATION_NAME;
		} else {
			return enumObj.getName();
		}
	}

	// ======================================================================
	// Package methods
	// ======================================================================

	/**
	 * Converts given ODS enumeration value using given enumeration class to the
	 * corresponding enumeration constant.
	 *
	 * @param <E>
	 *            The enumeration type.
	 * @param enumClass
	 *            The enumeration class.
	 * @param value
	 *            The ODS enumeration value.
	 * @return The corresponding enumeration constant is returned.
	 * @throws IllegalArgumentException
	 *             Thrown if conversion not possible.
	 */
	@SuppressWarnings("unchecked")
	//FIXME: cant we have this easier?
	static <E extends EnumerationValue> E fromODSEnum(Enumeration<E> enumObj, int value) {
		if (enumObj == null) {
			throw new IllegalArgumentException("EnumerationValue class is not allowed to be null.");
		} else if (EnumRegistry.SCALAR_TYPE.equals(enumObj.getName())) {
			return (E) fromODSScalarType(value);
		} else if (EnumRegistry.SEQUENCE_REPRESENTATION.equals(enumObj.getName())) {
			return (E) fromODSSequenceRepresentation(value);
		} else {
			return (E) fromODSEnumByOrdinal(enumObj, value);
		}
	}

	/**
	 * Converts given ODS enumeration values using given enumeration class to
	 * the corresponding enumeration constants.
	 *
	 * @param <E>
	 *            The enumeration type.
	 * @param enumClass
	 *            The enumeration class.
	 * @param values
	 *            The ODS enumeration values.
	 * @return The corresponding enumeration constants are returned.
	 * @throws IllegalArgumentException
	 *             Thrown if conversion not possible.
	 */
	@SuppressWarnings("unchecked")
	static <E extends EnumerationValue> E[] fromODSEnumSeq(Enumeration<?> enumObj, int[] values) {
		if (enumObj == null) {
			throw new IllegalArgumentException("EnumerationValue class is not allowed to be null.");
		} else if (EnumRegistry.SCALAR_TYPE.equals(enumObj.getName())) {
			List<E> scalarTypes = new ArrayList<>(values.length);
			for (int value : values) {
				scalarTypes.add((E) fromODSScalarType(value));
			}
			return (E[]) scalarTypes.toArray(new ScalarType[values.length]);
		} else if (EnumRegistry.SEQUENCE_REPRESENTATION.equals(enumObj.getName())) {
			List<E> sequenceRepresentations = new ArrayList<>(values.length);
			for (int value : values) {
				sequenceRepresentations.add((E) fromODSSequenceRepresentation(value));
			}
			return (E[]) sequenceRepresentations.toArray(new SequenceRepresentation[values.length]);
		} else {
			return (E[]) fromODSEnumSeqByOrdinal(enumObj, values);
		}
	}

	/**
	 * Converts given enumeration constant to the corresponding ODS enumeration
	 * value.
	 *
	 * @param <E>
	 *            The enumeration type.
	 * @param constant
	 *            The enumeration constant.
	 * @return The corresponding ODS enumeration value is returned.
	 * @throws IllegalArgumentException
	 *             Thrown if conversion not possible.
	 */
	static <E extends EnumerationValue> int toODSEnum(E constant) {
		if (constant == null) {
			return 0;
		} else if (constant instanceof ScalarType) {
			return toODSScalarType((ScalarType) constant);
		} else if (constant instanceof VersionState || constant instanceof Interpolation || constant instanceof AxisType
				|| constant instanceof TypeSpecification) {
			// NOTE: Ordinal numbers map directly to the corresponding ODS
			// enumeration constant value.
			return constant.ordinal();
		} else if (constant instanceof SequenceRepresentation) {
			return toODSSequenceRepresentation((SequenceRepresentation) constant);
		}

		throw new IllegalArgumentException(
				"EnumerationValue mapping for type '" + constant.getClass().getSimpleName() + "' does not exist.");
	}

	/**
	 * Converts given enumeration constants to the corresponding ODS enumeration
	 * values.
	 *
	 * @param <E>
	 *            The enumeration type.
	 * @param constants
	 *            The enumeration constants.
	 * @return The corresponding ODS enumeration values are returned.
	 * @throws IllegalArgumentException
	 *             Thrown if conversion not possible.
	 */
	static <E extends EnumerationValue> int[] toODSEnumSeq(E[] constants) {
		if (constants == null) {
			return new int[0];
		}

		int[] values = new int[constants.length];
		if (constants instanceof ScalarType[]) {
			for (int i = 0; i < values.length; i++) {
				values[i] = toODSScalarType((ScalarType) constants[i]);
			}
		} else if (constants instanceof VersionState[] || constants instanceof Interpolation[]
				|| constants instanceof AxisType[] || constants instanceof TypeSpecification[]) {
			for (int i = 0; i < values.length; i++) {
				// NOTE: Ordinal numbers directly map to the corresponding ODS
				// enumeration constant value.
				// FIXME: do we really want this?
				values[i] = ((EnumerationValue) constants[i]).ordinal();
			}
		} else if (constants instanceof SequenceRepresentation[]) {
			for (int i = 0; i < values.length; i++) {
				values[i] = toODSSequenceRepresentation((SequenceRepresentation) constants[i]);
			}
		} else {
			throw new IllegalArgumentException("EnumerationValue mapping for type '"
					+ constants.getClass().getComponentType().getSimpleName() + "' does not exist.");
		}

		return values;
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Converts given ODS enumeration value to the corresponding
	 * {@link ScalarType} constant.
	 *
	 * @param value
	 *            The ODS enumeration value.
	 * @return The corresponding {@code ScalarType} constant is returned.
	 * @throws IllegalArgumentException
	 *             Thrown if conversion not possible.
	 */
	private static ScalarType fromODSScalarType(int value) {
		if (value == 0) {
			return ScalarType.UNKNOWN;
		} else if (value == 1) {
			return ScalarType.STRING;
		} else if (value == 2) {
			return ScalarType.SHORT;
		} else if (value == 3) {
			return ScalarType.FLOAT;
		} else if (value == 4) {
			return ScalarType.BOOLEAN;
		} else if (value == 5) {
			return ScalarType.BYTE;
		} else if (value == 6) {
			return ScalarType.INTEGER;
		} else if (value == 7) {
			return ScalarType.DOUBLE;
		} else if (value == 8) {
			return ScalarType.LONG;
		} else if (value == 10) {
			return ScalarType.DATE;
		} else if (value == 11) {
			return ScalarType.BYTE_STREAM;
		} else if (value == 12) {
			return ScalarType.BLOB;
		} else if (value == 13) {
			return ScalarType.FLOAT_COMPLEX;
		} else if (value == 14) {
			return ScalarType.DOUBLE_COMPLEX;
			} else if (value == 28) {
			return ScalarType.FILE_LINK;
		} else if (value == 30) {
			return ScalarType.ENUMERATION;
		}

		throw new IllegalArgumentException("Unable to map ODS enumeration vaue '" + value + "' to constant of type '"
				+ ScalarType.class.getSimpleName() + "'.");
	}

	/**
	 * Converts given {@link ScalarType} to the corresponding ODS enumeration
	 * value.
	 *
	 * @param scalarType
	 *            The {@code ScalarType}.
	 * @return The corresponding ODS enumeration value is returned.
	 * @throws IllegalArgumentException
	 *             Thrown if conversion not possible.
	 */
	private static int toODSScalarType(ScalarType scalarType) {
		if (ScalarType.UNKNOWN == scalarType) {
			return 0;
		} else if (ScalarType.STRING == scalarType) {
			return 1;
		} else if (ScalarType.SHORT == scalarType) {
			return 2;
		} else if (ScalarType.FLOAT == scalarType) {
			return 3;
		} else if (ScalarType.BOOLEAN == scalarType) {
			return 4;
		} else if (ScalarType.BYTE == scalarType) {
			return 5;
		} else if (ScalarType.INTEGER == scalarType) {
			return 6;
		} else if (ScalarType.DOUBLE == scalarType) {
			return 7;
		} else if (ScalarType.LONG == scalarType) {
			return 8;
		} else if (ScalarType.DATE == scalarType) {
			return 10;
		} else if (ScalarType.BYTE_STREAM == scalarType) {
			return 11;
		} else if (ScalarType.BLOB == scalarType) {
			return 12;
		} else if (ScalarType.FLOAT_COMPLEX == scalarType) {
			return 13;
		} else if (ScalarType.DOUBLE_COMPLEX == scalarType) {
			return 14;
		} else if (ScalarType.FILE_LINK == scalarType) {
			return 28;
		} else if (ScalarType.ENUMERATION == scalarType) {
			return 30;
		}

		throw new IllegalArgumentException("Unable to map enumeration constant '" + scalarType + "' of type '"
				+ ScalarType.class.getSimpleName() + "' to ODS enumeration value.");
	}

	/**
	 * Converts given ODS enumeration value to the corresponding
	 * {@link SequenceRepresentation} constant.
	 *
	 * @param value
	 *            The ODS enumeration value.
	 * @return The corresponding {@code SequenceRepresentation} constant is
	 *         returned.
	 * @throws IllegalArgumentException
	 *             Thrown if conversion not possible.
	 */
	private static SequenceRepresentation fromODSSequenceRepresentation(int value) {
		if (value == 0) {
			return SequenceRepresentation.EXPLICIT;
		} else if (value == 1) {
			return SequenceRepresentation.IMPLICIT_CONSTANT;
		} else if (value == 2) {
			return SequenceRepresentation.IMPLICIT_LINEAR;
		} else if (value == 3) {
			return SequenceRepresentation.IMPLICIT_SAW;
		} else if (value == 4) {
			return SequenceRepresentation.RAW_LINEAR;
		} else if (value == 5) {
			return SequenceRepresentation.RAW_POLYNOMIAL;
		} else if (value == 7) {
			return SequenceRepresentation.EXPLICIT_EXTERNAL;
		} else if (value == 8) {
			return SequenceRepresentation.RAW_LINEAR_EXTERNAL;
		} else if (value == 9) {
			return SequenceRepresentation.RAW_POLYNOMIAL_EXTERNAL;
		} else if (value == 10) {
			return SequenceRepresentation.RAW_LINEAR_CALIBRATED;
		} else if (value == 11) {
			return SequenceRepresentation.RAW_LINEAR_CALIBRATED_EXTERNAL;
		}

		throw new IllegalArgumentException("Unable to map ODS enumeration vaue '" + value + "' to constant of type '"
				+ SequenceRepresentation.class.getSimpleName() + "'.");
	}

	/**
	 * Converts given {@link SequenceRepresentation} to the corresponding ODS
	 * enumeration value.
	 *
	 * @param sequenceRepresentation
	 *            The {@code SequenceRepresentation}.
	 * @return The corresponding ODS enumeration value is returned.
	 * @throws IllegalArgumentException
	 *             Thrown if conversion not possible.
	 */
	private static int toODSSequenceRepresentation(SequenceRepresentation sequenceRepresentation) {
		if (SequenceRepresentation.EXPLICIT == sequenceRepresentation) {
			return 0;
		} else if (SequenceRepresentation.IMPLICIT_CONSTANT == sequenceRepresentation) {
			return 1;
		} else if (SequenceRepresentation.IMPLICIT_LINEAR == sequenceRepresentation) {
			return 2;
		} else if (SequenceRepresentation.IMPLICIT_SAW == sequenceRepresentation) {
			return 3;
		} else if (SequenceRepresentation.RAW_LINEAR == sequenceRepresentation) {
			return 4;
		} else if (SequenceRepresentation.RAW_POLYNOMIAL == sequenceRepresentation) {
			return 5;
		} else if (SequenceRepresentation.EXPLICIT_EXTERNAL == sequenceRepresentation) {
			return 7;
		} else if (SequenceRepresentation.RAW_LINEAR_EXTERNAL == sequenceRepresentation) {
			return 8;
		} else if (SequenceRepresentation.RAW_POLYNOMIAL_EXTERNAL == sequenceRepresentation) {
			return 9;
		} else if (SequenceRepresentation.RAW_LINEAR_CALIBRATED == sequenceRepresentation) {
			return 10;
		} else if (SequenceRepresentation.RAW_LINEAR_CALIBRATED_EXTERNAL == sequenceRepresentation) {
			return 11;
		}

		throw new IllegalArgumentException("Unable to map enumeration constant '" + sequenceRepresentation
				+ "' of type '" + SequenceRepresentation.class.getSimpleName() + "' to ODS enumeration value.");
	}

	/**
	 * Converts given ODS enumeration value to the corresponding enumeration
	 * constant. The ODS enumeration value is used as the ordinal number of the
	 * requested enumeration constant.
	 *
	 * @param <E>
	 *            The enumeration type.
	 * @param enumClass
	 *            The enumeration class.
	 * @param value
	 *            The ODS enumeration value.
	 * @return The corresponding enumeration constant is returned.
	 * @throws IllegalArgumentException
	 *             Thrown if conversion not possible.
	 */
	private static <E extends EnumerationValue> E fromODSEnumByOrdinal(Enumeration<E> enumObj, int value) {
		// NOTE: Ordinal numbers directly map to the corresponding ODS
		// enumeration constant value.
		E enumvalue = enumObj.valueOf(value);
		if (enumvalue==null) {
			throw new IllegalArgumentException();
		}
		return enumvalue;
	}

	/**
	 * Converts given ODS enumeration values to the corresponding enumeration
	 * constants. The ODS enumeration values are used as the ordinal numbers of
	 * the requested enumeration constants.
	 *
	 * @param <E>
	 *            The enumeration type.
	 * @param enumClass
	 *            The enumeration class.
	 * @param values
	 *            The ODS enumeration values.
	 * @return The corresponding enumeration constants are returned.
	 * @throws IllegalArgumentException
	 *             Thrown if conversion not possible.
	 */
	@SuppressWarnings("unchecked")
	private static <E extends EnumerationValue> E[] fromODSEnumSeqByOrdinal(Enumeration<E> enumObj, int[] values) {
		List<E> enumValues = new ArrayList<>(values.length);

		for (int value : values) {
			// NOTE: Ordinal numbers directly map to the corresponding ODS
			// enumeration constant value.
			E enumvalue = enumObj.valueOf(value);
			if (enumvalue==null) {
				throw new IllegalArgumentException();
			}
			enumValues.add(enumvalue);
		}

		return enumValues.toArray((E[]) Array.newInstance(enumObj.getClass(), values.length));
	}

}
