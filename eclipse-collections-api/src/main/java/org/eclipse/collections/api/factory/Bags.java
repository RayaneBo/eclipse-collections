/*
 * Copyright (c) 2022 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.api.factory;

import org.eclipse.collections.api.factory.bag.ImmutableBagFactory;
import org.eclipse.collections.api.factory.bag.MutableBagFactory;
import org.eclipse.collections.api.factory.bag.MultiReaderBagFactory;
import org.eclipse.collections.api.factory.bag.MutableBagFactory;

/**
 * This class should be used to create instances of MutableBag and ImmutableBag
 * <p>
 * Mutable Examples:
 *
 * <pre>
 * MutableBag&lt;String&gt; emptyBag = Bags.mutable.empty();
 * MutableBag&lt;String&gt; bagWith = Bags.mutable.with("a", "b", "c");
 * MutableBag&lt;String&gt; bagOf = Bags.mutable.of("a", "b", "c");
 * </pre>
 *
 * Immutable Examples:
 *
 * <pre>
 * ImmutableBag&lt;String&gt; emptyBag = Bags.immutable.empty();
 * ImmutableBag&lt;String&gt; bagWith = Bags.immutable.with("a", "b", "c");
 * ImmutableBag&lt;String&gt; bagOf = Bags.immutable.of("a", "b", "c");
 * </pre>
 *
 * MultiReader Examples:
 *
 * <pre>
 * MultiReaderBag&lt;String&gt; emptyBag = Bags.multiReader.empty();
 * MultiReaderBag&lt;String&gt; bagWith = Bags.multiReader.with("a", "b", "c");
 * MultiReaderBag&lt;String&gt; bagOf = Bags.multiReader.of("a", "b", "c");
 * </pre>
 *
 */
@SuppressWarnings("ConstantNamingConvention")
@aQute.bnd.annotation.spi.ServiceConsumer(value = ImmutableBagFactory.class)
@aQute.bnd.annotation.spi.ServiceConsumer(value = MutableBagFactory.class)
@aQute.bnd.annotation.spi.ServiceConsumer(value = MultiReaderBagFactory.class)
public final class Bags
{
    public static final ImmutableBagFactory immutable = ServiceLoaderUtils.loadServiceClass(ImmutableBagFactory.class);
    public static final MutableBagFactory mutable = ServiceLoaderUtils.loadServiceClass(MutableBagFactory.class);
    public static final MultiReaderBagFactory multiReader = ServiceLoaderUtils.loadServiceClass(MultiReaderBagFactory.class);

    private Bags()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }
}
