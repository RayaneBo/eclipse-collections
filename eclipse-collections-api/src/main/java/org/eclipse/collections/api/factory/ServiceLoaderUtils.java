/*
 * Copyright (c) 2021 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.api.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * Classe utilitaire pour le chargement des services.
 * Cette classe utilise une stratégie de chargement configurable.
 */
public final class ServiceLoaderUtils
{
    // Messages d'erreur
    private static final String ERROR_NO_IMPLEMENTATION = "Could not find any implementations of %s. Check that eclipse-collections.jar is on the classpath and that its META-INF/services directory is intact.";
    private static final String ERROR_MULTIPLE_IMPLEMENTATIONS = "Found multiple implementations of %s on the classpath. Check that there is only one copy of eclipse-collections.jar on the classpath. Found implementations: %s.";

    // Map des implémentations de factory
    private static final Map<String, String> FACTORY_IMPLEMENTATIONS = new HashMap<>();

    static
    {
        // Initialisation des implémentations de factory
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.ImmutableBagFactory", "org.eclipse.collections.impl.bag.immutable.ImmutableBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.MultiReaderBagFactory", "org.eclipse.collections.impl.bag.mutable.MultiReaderMutableBagFactory");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.MutableBagFactory", "org.eclipse.collections.impl.bag.mutable.MutableBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.primitive.ImmutableBooleanBagFactory", "org.eclipse.collections.impl.bag.immutable.primitive.ImmutableBooleanBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.primitive.ImmutableByteBagFactory", "org.eclipse.collections.impl.bag.immutable.primitive.ImmutableByteBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.primitive.ImmutableCharBagFactory", "org.eclipse.collections.impl.bag.immutable.primitive.ImmutableCharBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.primitive.ImmutableDoubleBagFactory", "org.eclipse.collections.impl.bag.immutable.primitive.ImmutableDoubleBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.primitive.ImmutableFloatBagFactory", "org.eclipse.collections.impl.bag.immutable.primitive.ImmutableFloatBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.primitive.ImmutableIntBagFactory", "org.eclipse.collections.impl.bag.immutable.primitive.ImmutableIntBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.primitive.ImmutableLongBagFactory", "org.eclipse.collections.impl.bag.immutable.primitive.ImmutableLongBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.primitive.ImmutableShortBagFactory", "org.eclipse.collections.impl.bag.immutable.primitive.ImmutableShortBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.primitive.MutableBooleanBagFactory", "org.eclipse.collections.impl.bag.mutable.primitive.MutableBooleanBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.primitive.MutableByteBagFactory", "org.eclipse.collections.impl.bag.mutable.primitive.MutableByteBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.primitive.MutableCharBagFactory", "org.eclipse.collections.impl.bag.mutable.primitive.MutableCharBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.primitive.MutableDoubleBagFactory", "org.eclipse.collections.impl.bag.mutable.primitive.MutableDoubleBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.primitive.MutableFloatBagFactory", "org.eclipse.collections.impl.bag.mutable.primitive.MutableFloatBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.primitive.MutableIntBagFactory", "org.eclipse.collections.impl.bag.mutable.primitive.MutableIntBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.primitive.MutableLongBagFactory", "org.eclipse.collections.impl.bag.mutable.primitive.MutableLongBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.primitive.MutableShortBagFactory", "org.eclipse.collections.impl.bag.mutable.primitive.MutableShortBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.sorted.ImmutableSortedBagFactory", "org.eclipse.collections.impl.bag.sorted.immutable.ImmutableSortedBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bag.sorted.MutableSortedBagFactory", "org.eclipse.collections.impl.bag.sorted.mutable.MutableSortedBagFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bimap.ImmutableBiMapFactory", "org.eclipse.collections.impl.bimap.immutable.ImmutableBiMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.bimap.MutableBiMapFactory", "org.eclipse.collections.impl.bimap.mutable.MutableBiMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.FixedSizeListFactory", "org.eclipse.collections.impl.list.fixed.FixedSizeListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.ImmutableListFactory", "org.eclipse.collections.impl.list.immutable.ImmutableListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.MultiReaderListFactory", "org.eclipse.collections.impl.list.mutable.MultiReaderMutableListFactory");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.MutableListFactory", "org.eclipse.collections.impl.list.mutable.MutableListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.primitive.ImmutableBooleanListFactory", "org.eclipse.collections.impl.list.immutable.primitive.ImmutableBooleanListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.primitive.ImmutableByteListFactory", "org.eclipse.collections.impl.list.immutable.primitive.ImmutableByteListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.primitive.ImmutableCharListFactory", "org.eclipse.collections.impl.list.immutable.primitive.ImmutableCharListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.primitive.ImmutableDoubleListFactory", "org.eclipse.collections.impl.list.immutable.primitive.ImmutableDoubleListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.primitive.ImmutableFloatListFactory", "org.eclipse.collections.impl.list.immutable.primitive.ImmutableFloatListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.primitive.ImmutableIntListFactory", "org.eclipse.collections.impl.list.immutable.primitive.ImmutableIntListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.primitive.ImmutableLongListFactory", "org.eclipse.collections.impl.list.immutable.primitive.ImmutableLongListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.primitive.ImmutableShortListFactory", "org.eclipse.collections.impl.list.immutable.primitive.ImmutableShortListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.primitive.MutableBooleanListFactory", "org.eclipse.collections.impl.list.mutable.primitive.MutableBooleanListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.primitive.MutableByteListFactory", "org.eclipse.collections.impl.list.mutable.primitive.MutableByteListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.primitive.MutableCharListFactory", "org.eclipse.collections.impl.list.mutable.primitive.MutableCharListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.primitive.MutableDoubleListFactory", "org.eclipse.collections.impl.list.mutable.primitive.MutableDoubleListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.primitive.MutableFloatListFactory", "org.eclipse.collections.impl.list.mutable.primitive.MutableFloatListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.primitive.MutableIntListFactory", "org.eclipse.collections.impl.list.mutable.primitive.MutableIntListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.primitive.MutableLongListFactory", "org.eclipse.collections.impl.list.mutable.primitive.MutableLongListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.list.primitive.MutableShortListFactory", "org.eclipse.collections.impl.list.mutable.primitive.MutableShortListFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.FixedSizeMapFactory", "org.eclipse.collections.impl.map.fixed.FixedSizeMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.ImmutableMapFactory", "org.eclipse.collections.impl.map.immutable.ImmutableMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.MutableMapFactory", "org.eclipse.collections.impl.map.mutable.MutableMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableBooleanBooleanMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableBooleanBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableBooleanByteMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableBooleanByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableBooleanCharMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableBooleanCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableBooleanDoubleMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableBooleanDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableBooleanFloatMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableBooleanFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableBooleanIntMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableBooleanIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableBooleanLongMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableBooleanLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableBooleanShortMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableBooleanShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableByteBooleanMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableByteBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableByteByteMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableByteByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableByteCharMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableByteCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableByteDoubleMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableByteDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableByteFloatMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableByteFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableByteIntMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableByteIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableByteLongMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableByteLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableByteObjectMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableByteObjectMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableByteShortMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableByteShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableCharBooleanMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableCharBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableCharByteMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableCharByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableCharCharMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableCharCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableCharDoubleMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableCharDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableCharFloatMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableCharFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableCharIntMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableCharIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableCharLongMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableCharLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableCharObjectMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableCharObjectMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableCharShortMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableCharShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableDoubleBooleanMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableDoubleBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableDoubleByteMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableDoubleByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableDoubleCharMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableDoubleCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableDoubleDoubleMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableDoubleDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableDoubleFloatMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableDoubleFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableDoubleIntMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableDoubleIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableDoubleLongMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableDoubleLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableDoubleObjectMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableDoubleObjectMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableDoubleShortMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableDoubleShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableFloatBooleanMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableFloatBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableFloatByteMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableFloatByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableFloatCharMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableFloatCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableFloatDoubleMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableFloatDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableFloatFloatMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableFloatFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableFloatIntMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableFloatIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableFloatLongMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableFloatLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableFloatObjectMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableFloatObjectMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableFloatShortMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableFloatShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableIntBooleanMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableIntByteMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableIntCharMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableIntDoubleMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableIntFloatMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableIntIntMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableIntLongMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableIntObjectMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntObjectMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableIntShortMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableIntShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableLongBooleanMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableLongByteMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableLongCharMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableLongDoubleMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableLongFloatMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableLongIntMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableLongLongMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableLongObjectMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongObjectMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableLongShortMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableLongShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableObjectBooleanMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableObjectBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableObjectByteMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableObjectByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableObjectCharMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableObjectCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableObjectDoubleMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableObjectDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableObjectFloatMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableObjectFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableObjectIntMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableObjectIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableObjectLongMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableObjectLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableObjectShortMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableObjectShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableShortBooleanMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableShortBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableShortByteMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableShortByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableShortCharMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableShortCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableShortDoubleMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableShortDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableShortFloatMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableShortFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableShortIntMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableShortIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableShortLongMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableShortLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableShortObjectMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableShortObjectMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.ImmutableShortShortMapFactory", "org.eclipse.collections.impl.map.immutable.primitive.ImmutableShortShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableBooleanBooleanMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableBooleanBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableBooleanByteMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableBooleanByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableBooleanCharMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableBooleanCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableBooleanDoubleMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableBooleanDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableBooleanFloatMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableBooleanFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableBooleanIntMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableBooleanIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableBooleanLongMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableBooleanLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableBooleanShortMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableBooleanShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableByteBooleanMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableByteBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableByteByteMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableByteByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableByteCharMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableByteCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableByteDoubleMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableByteDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableByteFloatMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableByteFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableByteIntMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableByteIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableByteLongMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableByteLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableByteObjectMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableByteObjectMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableByteShortMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableByteShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableCharBooleanMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableCharBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableCharByteMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableCharByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableCharCharMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableCharCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableCharDoubleMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableCharDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableCharFloatMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableCharFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableCharIntMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableCharIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableCharLongMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableCharLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableCharObjectMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableCharObjectMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableCharShortMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableCharShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableDoubleBooleanMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableDoubleBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableDoubleByteMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableDoubleByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableDoubleCharMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableDoubleCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableDoubleDoubleMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableDoubleDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableDoubleFloatMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableDoubleFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableDoubleIntMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableDoubleIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableDoubleLongMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableDoubleLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableDoubleObjectMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableDoubleObjectMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableDoubleShortMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableDoubleShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableFloatBooleanMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableFloatBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableFloatByteMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableFloatByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableFloatCharMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableFloatCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableFloatDoubleMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableFloatDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableFloatFloatMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableFloatFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableFloatIntMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableFloatIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableFloatLongMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableFloatLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableFloatObjectMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableFloatObjectMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableFloatShortMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableFloatShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableIntBooleanMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableIntBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableIntByteMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableIntByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableIntCharMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableIntCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableIntDoubleMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableIntDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableIntFloatMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableIntFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableIntIntMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableIntIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableIntLongMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableIntLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableIntObjectMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableIntObjectMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableIntShortMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableIntShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableLongBooleanMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableLongBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableLongByteMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableLongByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableLongCharMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableLongCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableLongDoubleMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableLongDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableLongFloatMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableLongFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableLongIntMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableLongIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableLongLongMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableLongLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableLongObjectMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableLongObjectMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableLongShortMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableLongShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableObjectBooleanHashingStrategyMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableObjectBooleanHashingStrategyMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableObjectBooleanMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableObjectBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableObjectByteHashingStrategyMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableObjectByteHashingStrategyMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableObjectByteMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableObjectByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableObjectCharHashingStrategyMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableObjectCharHashingStrategyMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableObjectCharMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableObjectCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableObjectDoubleHashingStrategyMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableObjectDoubleHashingStrategyMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableObjectDoubleMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableObjectDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableObjectFloatHashingStrategyMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableObjectFloatHashingStrategyMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableObjectFloatMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableObjectFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableObjectIntHashingStrategyMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableObjectIntHashingStrategyMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableObjectIntMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableObjectIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableObjectLongHashingStrategyMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableObjectLongHashingStrategyMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableObjectLongMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableObjectLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableObjectShortHashingStrategyMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableObjectShortHashingStrategyMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableObjectShortMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableObjectShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableShortBooleanMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableShortBooleanMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableShortByteMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableShortByteMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableShortCharMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableShortCharMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableShortDoubleMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableShortDoubleMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableShortFloatMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableShortFloatMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableShortIntMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableShortIntMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableShortLongMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableShortLongMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableShortObjectMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableShortObjectMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.primitive.MutableShortShortMapFactory", "org.eclipse.collections.impl.map.mutable.primitive.MutableShortShortMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.sorted.ImmutableSortedMapFactory", "org.eclipse.collections.impl.map.sorted.immutable.ImmutableSortedMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.map.sorted.MutableSortedMapFactory", "org.eclipse.collections.impl.map.sorted.mutable.MutableSortedMapFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.FixedSizeSetFactory", "org.eclipse.collections.impl.set.fixed.FixedSizeSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.ImmutableSetFactory", "org.eclipse.collections.impl.set.immutable.ImmutableSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.MultiReaderSetFactory", "org.eclipse.collections.impl.set.mutable.MultiReaderMutableSetFactory");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.MutableSetFactory", "org.eclipse.collections.impl.set.mutable.MutableSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.primitive.ImmutableBooleanSetFactory", "org.eclipse.collections.impl.set.immutable.primitive.ImmutableBooleanSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.primitive.ImmutableByteSetFactory", "org.eclipse.collections.impl.set.immutable.primitive.ImmutableByteSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.primitive.ImmutableCharSetFactory", "org.eclipse.collections.impl.set.immutable.primitive.ImmutableCharSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.primitive.ImmutableDoubleSetFactory", "org.eclipse.collections.impl.set.immutable.primitive.ImmutableDoubleSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.primitive.ImmutableFloatSetFactory", "org.eclipse.collections.impl.set.immutable.primitive.ImmutableFloatSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.primitive.ImmutableIntSetFactory", "org.eclipse.collections.impl.set.immutable.primitive.ImmutableIntSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.primitive.ImmutableLongSetFactory", "org.eclipse.collections.impl.set.immutable.primitive.ImmutableLongSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.primitive.ImmutableShortSetFactory", "org.eclipse.collections.impl.set.immutable.primitive.ImmutableShortSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.primitive.MutableBooleanSetFactory", "org.eclipse.collections.impl.set.mutable.primitive.MutableBooleanSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.primitive.MutableByteSetFactory", "org.eclipse.collections.impl.set.mutable.primitive.MutableByteSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.primitive.MutableCharSetFactory", "org.eclipse.collections.impl.set.mutable.primitive.MutableCharSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.primitive.MutableDoubleSetFactory", "org.eclipse.collections.impl.set.mutable.primitive.MutableDoubleSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.primitive.MutableFloatSetFactory", "org.eclipse.collections.impl.set.mutable.primitive.MutableFloatSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.primitive.MutableIntSetFactory", "org.eclipse.collections.impl.set.mutable.primitive.MutableIntSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.primitive.MutableLongSetFactory", "org.eclipse.collections.impl.set.mutable.primitive.MutableLongSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.primitive.MutableShortSetFactory", "org.eclipse.collections.impl.set.mutable.primitive.MutableShortSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.sorted.ImmutableSortedSetFactory", "org.eclipse.collections.impl.set.sorted.immutable.ImmutableSortedSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.set.sorted.MutableSortedSetFactory", "org.eclipse.collections.impl.set.sorted.mutable.MutableSortedSetFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.ImmutableStackFactory", "org.eclipse.collections.impl.stack.immutable.ImmutableStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.MutableStackFactory", "org.eclipse.collections.impl.stack.mutable.MutableStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.primitive.ImmutableBooleanStackFactory", "org.eclipse.collections.impl.stack.immutable.primitive.ImmutableBooleanStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.primitive.ImmutableByteStackFactory", "org.eclipse.collections.impl.stack.immutable.primitive.ImmutableByteStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.primitive.ImmutableCharStackFactory", "org.eclipse.collections.impl.stack.immutable.primitive.ImmutableCharStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.primitive.ImmutableDoubleStackFactory", "org.eclipse.collections.impl.stack.immutable.primitive.ImmutableDoubleStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.primitive.ImmutableFloatStackFactory", "org.eclipse.collections.impl.stack.immutable.primitive.ImmutableFloatStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.primitive.ImmutableIntStackFactory", "org.eclipse.collections.impl.stack.immutable.primitive.ImmutableIntStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.primitive.ImmutableLongStackFactory", "org.eclipse.collections.impl.stack.immutable.primitive.ImmutableLongStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.primitive.ImmutableShortStackFactory", "org.eclipse.collections.impl.stack.immutable.primitive.ImmutableShortStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.primitive.MutableBooleanStackFactory", "org.eclipse.collections.impl.stack.mutable.primitive.MutableBooleanStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.primitive.MutableByteStackFactory", "org.eclipse.collections.impl.stack.mutable.primitive.MutableByteStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.primitive.MutableCharStackFactory", "org.eclipse.collections.impl.stack.mutable.primitive.MutableCharStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.primitive.MutableDoubleStackFactory", "org.eclipse.collections.impl.stack.mutable.primitive.MutableDoubleStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.primitive.MutableFloatStackFactory", "org.eclipse.collections.impl.stack.mutable.primitive.MutableFloatStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.primitive.MutableIntStackFactory", "org.eclipse.collections.impl.stack.mutable.primitive.MutableIntStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.primitive.MutableLongStackFactory", "org.eclipse.collections.impl.stack.mutable.primitive.MutableLongStackFactoryImpl");
        FACTORY_IMPLEMENTATIONS.put("org.eclipse.collections.api.factory.stack.primitive.MutableShortStackFactory", "org.eclipse.collections.impl.stack.mutable.primitive.MutableShortStackFactoryImpl");
    }

    private static final ServiceLoaderStrategy<?> DEFAULT_STRATEGY = new DefaultServiceLoaderStrategy<>();

    private ServiceLoaderUtils()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    /**
     * Charge une implémentation de service en utilisant la stratégie par défaut.
     *
     * @param serviceClass La classe du service à charger
     * @param <T> Le type du service
     * @return Une instance du service
     * @throws IllegalArgumentException si la classe de service est null
     */
    @SuppressWarnings("unchecked")
    public static <T> T loadServiceClass(Class<T> serviceClass)
    {
        return ((ServiceLoaderStrategy<T>) DEFAULT_STRATEGY).loadServiceClass(serviceClass);
    }

    /**
     * Charge une implémentation de service en utilisant une stratégie personnalisée.
     *
     * @param serviceClass La classe du service à charger
     * @param strategy La stratégie de chargement à utiliser
     * @param <T> Le type du service
     * @return Une instance du service
     * @throws IllegalArgumentException si la classe de service ou la stratégie est null
     */
    public static <T> T loadServiceClass(Class<T> serviceClass, ServiceLoaderStrategy<T> strategy)
    {
        if (strategy == null)
        {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        return strategy.loadServiceClass(serviceClass);
    }
}
