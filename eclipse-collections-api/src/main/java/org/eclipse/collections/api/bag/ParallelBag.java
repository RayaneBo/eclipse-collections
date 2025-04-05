/*
 * Copyright (c) 2021 Goldman Sachs.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.api.bag;

import org.eclipse.collections.api.ParallelIterable;
import org.eclipse.collections.api.annotation.Beta;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.block.procedure.primitive.ObjectIntProcedure;
import org.eclipse.collections.api.multimap.bag.BagMultimap;

/**
 * Une ParallelBag est un décorateur qui ajoute des fonctionnalités de traitement parallèle à une Bag.
 * Elle permet d'exécuter des opérations sur les éléments de la bag en parallèle.
 *
 * @since 5.0
 */
@Beta
public interface ParallelBag<T> extends ParallelIterable<T>
{
    /**
     * Exécute la procédure spécifiée pour chaque élément distinct et son nombre d'occurrences en parallèle.
     *
     * @param procedure la procédure à exécuter pour chaque élément distinct et son compte
     */
    void forEachWithOccurrences(ObjectIntProcedure<? super T> procedure);

    /**
     * Crée une ParallelBag pour sélectionner des éléments de la bag actuelle.
     */
    @Override
    ParallelBag<T> select(Predicate<? super T> predicate);

    @Override
    <P> ParallelBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    /**
     * Crée une ParallelBag pour rejeter des éléments de la bag actuelle.
     */
    @Override
    ParallelBag<T> reject(Predicate<? super T> predicate);

    @Override
    <P> ParallelBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    <S> ParallelBag<S> selectInstancesOf(Class<S> clazz);

    @Override
    <V> BagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    @Override
    <V> BagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    /**
     * Retourne la bag sous-jacente.
     *
     * @return la bag sous-jacente
     */
    Bag<T> getUnderlyingBag();
}
