/*
 * Copyright (c) 2021 Goldman Sachs.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.parallel;

import java.util.Collection;

/**
 * Une classe utilitaire pour créer des instances de {@link Combiner}.
 */
public final class Combiners
{
    private Combiners()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    /**
     * Crée un combinateur qui ne fait rien.
     *
     * @param <T> Le type des éléments
     * @return Un combinateur qui ne fait rien
     */
    public static <T> Combiner<T, Void> passThru()
    {
        return new Combiner<>(new PassThruCombinationStrategy<>());
    }

    /**
     * Crée un combinateur pour les collections.
     *
     * @param targetCollection La collection cible
     * @param useCombineOne Si la combinaison doit être faite un par un
     * @param <T> Le type des éléments
     * @return Un combinateur pour les collections
     */
    public static <T> Combiner<Collection<T>, Collection<T>> collection(Collection<T> targetCollection, boolean useCombineOne)
    {
        return new Combiner<>(new CollectionCombinationStrategy<>(targetCollection, useCombineOne));
    }

    /**
     * Crée un combinateur pour les collections avec combinaison un par un.
     *
     * @param targetCollection La collection cible
     * @param <T> Le type des éléments
     * @return Un combinateur pour les collections
     */
    public static <T> Combiner<Collection<T>, Collection<T>> collection(Collection<T> targetCollection)
    {
        return collection(targetCollection, true);
    }
}
