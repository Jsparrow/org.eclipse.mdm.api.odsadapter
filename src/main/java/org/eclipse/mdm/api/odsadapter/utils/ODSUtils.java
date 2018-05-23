/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.utils;

import com.google.common.collect.ImmutableBiMap;
import org.asam.ods.AggrFunc;
import org.asam.ods.DataType;
import org.asam.ods.SelOpcode;
import org.asam.ods.SelOperator;
import org.eclipse.mdm.api.base.adapter.RelationType;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.Aggregation;
import org.eclipse.mdm.api.base.query.BracketOperator;
import org.eclipse.mdm.api.base.query.JoinType;
import org.eclipse.mdm.api.base.query.ComparisonOperator;
import org.eclipse.mdm.api.base.query.BooleanOperator;

/**
 * Utility class provides bidirectional mappings for ODS types
 *
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 * @since 1.0.0
 */
public abstract class ODSUtils {
    /**
     * Maps {@link RelationType} to the corresponding ODS {@link RelationType}.
     */
    public static final ImmutableBiMap<RelationType, org.asam.ods.RelationType> RELATIONSHIPS =
            ImmutableBiMap.<RelationType, org.asam.ods.RelationType>builder()
                    .put(RelationType.FATHER_CHILD, org.asam.ods.RelationType.FATHER_CHILD)
                    .put(RelationType.INFO, org.asam.ods.RelationType.INFO)
                    .put(RelationType.INHERITANCE, org.asam.ods.RelationType.INHERITANCE)
                    .build();

    /**
     * Maps {@link Aggregation} to the corresponding ODS {@link AggrFunc}.
     */
    public static final ImmutableBiMap<Aggregation, AggrFunc> AGGREGATIONS =
            ImmutableBiMap.<Aggregation, AggrFunc>builder()
                    .put(Aggregation.NONE, AggrFunc.NONE)
                    .put(Aggregation.COUNT, AggrFunc.COUNT)
                    .put(Aggregation.DISTINCT_COUNT, AggrFunc.DCOUNT)
                    .put(Aggregation.MINIMUM, AggrFunc.MIN)
                    .put(Aggregation.MAXIMUM, AggrFunc.MAX)
                    .put(Aggregation.AVERAGE, AggrFunc.AVG)
                    .put(Aggregation.DEVIATION, AggrFunc.STDDEV)
                    .put(Aggregation.SUM, AggrFunc.SUM)
                    .put(Aggregation.DISTINCT, AggrFunc.DISTINCT)
                    .build();

    /**
     * Maps {@link ContextType} to the corresponding ODS {@link String}.
     */
    public static final ImmutableBiMap<ContextType, String> CONTEXTTYPES =
            ImmutableBiMap.<ContextType, String>builder()
                    .put(ContextType.UNITUNDERTEST, "UnitUnderTest")
                    .put(ContextType.TESTSEQUENCE, "TestSequence")
                    .put(ContextType.TESTEQUIPMENT, "TestEquipment")
                    .build();

    /**
     * Maps {@link ComparisonOperator} to the corresponding ODS {@link SelOpcode}.
     */
    public static final ImmutableBiMap<ComparisonOperator, SelOpcode> OPERATIONS =
            ImmutableBiMap.<ComparisonOperator, SelOpcode>builder()
                    .put(ComparisonOperator.LIKE, SelOpcode.LIKE)
                    .put(ComparisonOperator.CASE_INSENSITIVE_LIKE, SelOpcode.CI_LIKE)
                    .put(ComparisonOperator.NOT_LIKE, SelOpcode.NOTLIKE)
                    .put(ComparisonOperator.CASE_INSENSITIVE_NOT_LIKE, SelOpcode.CI_NOTLIKE)
                    .put(ComparisonOperator.EQUAL, SelOpcode.EQ)
                    .put(ComparisonOperator.CASE_INSENSITIVE_EQUAL, SelOpcode.CI_EQ)
                    .put(ComparisonOperator.NOT_EQUAL, SelOpcode.NEQ)
                    .put(ComparisonOperator.CASE_INSENSITIVE_NOT_EQUAL, SelOpcode.CI_NEQ)
                    .put(ComparisonOperator.GREATER_THAN, SelOpcode.GT)
                    .put(ComparisonOperator.CASE_INSENSITIVE_GREATER_THAN, SelOpcode.CI_GT)
                    .put(ComparisonOperator.GREATER_THAN_OR_EQUAL, SelOpcode.GTE)
                    .put(ComparisonOperator.CASE_INSENSITIVE_GREATER_THAN_OR_EQUAL, SelOpcode.CI_GTE)
                    .put(ComparisonOperator.LESS_THAN, SelOpcode.LT)
                    .put(ComparisonOperator.CASE_INSENSITIVE_LESS_THAN, SelOpcode.CI_LT)
                    .put(ComparisonOperator.LESS_THAN_OR_EQUAL, SelOpcode.LTE)
                    .put(ComparisonOperator.CASE_INSENSITIVE_LESS_THAN_OR_EQUAL, SelOpcode.CI_LTE)
                    .put(ComparisonOperator.IS_NULL, SelOpcode.IS_NULL)
                    .put(ComparisonOperator.IS_NOT_NULL, SelOpcode.IS_NOT_NULL)
                    .put(ComparisonOperator.IN_SET, SelOpcode.INSET)
                    .put(ComparisonOperator.CASE_INSENSITIVE_IN_SET, SelOpcode.CI_INSET)
                    .put(ComparisonOperator.NOT_IN_SET, SelOpcode.NOTINSET)
                    .put(ComparisonOperator.CASE_INSENSITIVE_NOT_IN_SET, SelOpcode.CI_NOTINSET)
                    .put(ComparisonOperator.BETWEEN, SelOpcode.BETWEEN)
                    .build();

    /**
     * Maps {@link BooleanOperator} to the corresponding ODS {@link SelOperator}.
     */
    public static final ImmutableBiMap<BooleanOperator, SelOperator> OPERATORS =
            ImmutableBiMap.<BooleanOperator, SelOperator>builder()
                    .put(BooleanOperator.AND, SelOperator.AND)
                    .put(BooleanOperator.OR, SelOperator.OR)
                    .put(BooleanOperator.NOT, SelOperator.NOT)
                    .build();

    /**
     * Maps {@link BracketOperator} to the corresponding ODS {@link SelOperator}.
     */
    public static final ImmutableBiMap<BracketOperator, SelOperator> BRACKETOPERATORS =
            ImmutableBiMap.<BracketOperator, SelOperator>builder()
                    .put(BracketOperator.OPEN, SelOperator.OPEN)
                    .put(BracketOperator.CLOSE, SelOperator.CLOSE)
                    .build();

    /**
     * Maps {@link ValueType} to the corresponding ODS {@link DataType}.
     */
    public static final ImmutableBiMap<ValueType<?>, DataType> VALUETYPES =
            ImmutableBiMap.<ValueType<?>, DataType> builder()
                    .put(ValueType.UNKNOWN, DataType.DT_UNKNOWN)
                    .put(ValueType.STRING, DataType.DT_STRING)
                    .put(ValueType.STRING_SEQUENCE, DataType.DS_STRING)
                    .put(ValueType.DATE, DataType.DT_DATE)
                    .put(ValueType.DATE_SEQUENCE, DataType.DS_DATE)
                    .put(ValueType.BOOLEAN, DataType.DT_BOOLEAN)
                    .put(ValueType.BOOLEAN_SEQUENCE, DataType.DS_BOOLEAN)
                    .put(ValueType.BYTE, DataType.DT_BYTE)
                    .put(ValueType.BYTE_SEQUENCE, DataType.DS_BYTE)
                    .put(ValueType.SHORT, DataType.DT_SHORT)
                    .put(ValueType.SHORT_SEQUENCE, DataType.DS_SHORT)
                    .put(ValueType.INTEGER, DataType.DT_LONG)
                    .put(ValueType.INTEGER_SEQUENCE, DataType.DS_LONG)
                    .put(ValueType.LONG, DataType.DT_LONGLONG)
                    .put(ValueType.LONG_SEQUENCE, DataType.DS_LONGLONG)
                    .put(ValueType.FLOAT, DataType.DT_FLOAT)
                    .put(ValueType.FLOAT_SEQUENCE, DataType.DS_FLOAT)
                    .put(ValueType.DOUBLE, DataType.DT_DOUBLE)
                    .put(ValueType.DOUBLE_SEQUENCE, DataType.DS_DOUBLE)
                    .put(ValueType.BYTE_STREAM, DataType.DT_BYTESTR)
                    .put(ValueType.BYTE_STREAM_SEQUENCE, DataType.DS_BYTESTR)
                    .put(ValueType.FLOAT_COMPLEX, DataType.DT_COMPLEX)
                    .put(ValueType.FLOAT_COMPLEX_SEQUENCE, DataType.DS_COMPLEX)
                    .put(ValueType.DOUBLE_COMPLEX, DataType.DT_DCOMPLEX)
                    .put(ValueType.DOUBLE_COMPLEX_SEQUENCE, DataType.DS_DCOMPLEX)
                    .put(ValueType.ENUMERATION, DataType.DT_ENUM)
                    .put(ValueType.ENUMERATION_SEQUENCE, DataType.DS_ENUM)
                    .put(ValueType.FILE_LINK, DataType.DT_EXTERNALREFERENCE)
                    .put(ValueType.FILE_LINK_SEQUENCE, DataType.DS_EXTERNALREFERENCE)
                    .put(ValueType.BLOB, DataType.DT_BLOB)
                    .build();

    /**
     * Maps {@link JoinType} to the corresponding ODS {@link org.asam.ods.JoinType}.
     */
    public static final ImmutableBiMap<JoinType, org.asam.ods.JoinType> JOINS =
            ImmutableBiMap.<JoinType, org.asam.ods.JoinType>builder()
                    .put(JoinType.INNER, org.asam.ods.JoinType.JTDEFAULT)
                    .put(JoinType.OUTER, org.asam.ods.JoinType.JTOUTER)
                    .build();

    public static boolean isValidID(String instanceID) {
        return instanceID != null && !instanceID.isEmpty() && !"0".equals(instanceID);
    }
}
