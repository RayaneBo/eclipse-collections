/*
 * Copyright (c) 2021 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.api.bag;

import org.eclipse.collections.api.bag.primitive.ImmutableBooleanBag;
import org.eclipse.collections.api.bag.primitive.ImmutableByteBag;
import org.eclipse.collections.api.bag.primitive.ImmutableCharBag;
import org.eclipse.collections.api.bag.primitive.ImmutableDoubleBag;
import org.eclipse.collections.api.bag.primitive.ImmutableFloatBag;
import org.eclipse.collections.api.bag.primitive.ImmutableIntBag;
import org.eclipse.collections.api.bag.primitive.ImmutableLongBag;
import org.eclipse.collections.api.bag.primitive.ImmutableShortBag;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.function.primitive.BooleanFunction;
import org.eclipse.collections.api.block.function.primitive.ByteFunction;
import org.eclipse.collections.api.block.function.primitive.CharFunction;
import org.eclipse.collections.api.block.function.primitive.DoubleFunction;
import org.eclipse.collections.api.block.function.primitive.FloatFunction;
import org.eclipse.collections.api.block.function.primitive.IntFunction;
import org.eclipse.collections.api.block.function.primitive.LongFunction;
import org.eclipse.collections.api.block.function.primitive.ObjectIntToObjectFunction;
import org.eclipse.collections.api.block.function.primitive.ShortFunction;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.block.predicate.primitive.IntPredicate;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.collection.ImmutableCollection;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.multimap.bag.ImmutableBagMultimap;
import org.eclipse.collections.api.ordered.OrderedIterable;
import org.eclipse.collections.api.partition.bag.PartitionImmutableBag;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.api.tuple.primitive.ObjectIntPair;

/**
 * Une ImmutableBag est une Collection dont les éléments sont non ordonnés et peuvent contenir des entrées en double.
 * Elle diffère de ImmutableCollection en ce qu'elle ajoute un protocole pour déterminer le nombre d'occurrences d'un élément.
 * Une ImmutableBag ne peut pas être modifiée après sa création.
 *
 * @since 1.0
 */
public interface ImmutableBag<T>
        extends Bag<T>, ImmutableCollection<T>
{
    @Override
    ImmutableMap<T, Integer> toMapOfItemToCount();

    @Override
    ImmutableBag<T> selectByOccurrences(IntPredicate predicate);

    /**
     * @since 9.2
     */
    @Override
    default ImmutableBag<T> selectDuplicates()
    {
        return this.selectByOccurrences(occurrences -> occurrences > 1);
    }

    /**
     * @since 9.2
     */
    @Override
    default ImmutableSet<T> selectUnique()
    {
        MutableSet<T> result = Sets.mutable.empty();
        this.forEachWithOccurrences((each, occurrences) ->
        {
            if (occurrences == 1)
            {
                result.add(each);
            }
        });
        return result.toImmutable();
    }

    @Override
    ImmutableBag<T> tap(Procedure<? super T> procedure);

    @Override
    ImmutableBag<T> select(Predicate<? super T> predicate);

    @Override
    <P> ImmutableBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    ImmutableBag<T> reject(Predicate<? super T> predicate);

    @Override
    <P> ImmutableBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    PartitionImmutableBag<T> partition(Predicate<? super T> predicate);

    @Override
    <P> PartitionImmutableBag<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    <V> ImmutableBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    @Override
    <V> ImmutableBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    @Override
    ImmutableSet<Pair<T, Integer>> zipWithIndex();

    /**
     * Retourne une nouvelle ImmutableBag avec l'élément ajouté.
     *
     * @param element l'élément à ajouter
     * @return une nouvelle ImmutableBag avec l'élément ajouté
     */
    ImmutableBag<T> newWith(T element);

    /**
     * Retourne une nouvelle ImmutableBag avec l'élément supprimé.
     *
     * @param element l'élément à supprimer
     * @return une nouvelle ImmutableBag avec l'élément supprimé
     */
    ImmutableBag<T> newWithout(T element);

    /**
     * Retourne une nouvelle ImmutableBag avec tous les éléments ajoutés.
     *
     * @param elements les éléments à ajouter
     * @return une nouvelle ImmutableBag avec tous les éléments ajoutés
     */
    ImmutableBag<T> newWithAll(Iterable<? extends T> elements);

    /**
     * Retourne une nouvelle ImmutableBag avec tous les éléments supprimés.
     *
     * @param elements les éléments à supprimer
     * @return une nouvelle ImmutableBag avec tous les éléments supprimés
     */
    ImmutableBag<T> newWithoutAll(Iterable<? extends T> elements);

    /**
     * Retourne une nouvelle ImmutableBag avec le nombre d'occurrences de l'élément modifié.
     *
     * @param element l'élément dont le nombre d'occurrences doit être modifié
     * @param occurrences le nouveau nombre d'occurrences
     * @return une nouvelle ImmutableBag avec le nombre d'occurrences modifié
     */
    ImmutableBag<T> newWithOccurrences(T element, int occurrences);

    /**
     * Retourne une nouvelle ImmutableBag avec le nombre d'occurrences de l'élément supprimé.
     *
     * @param element l'élément dont les occurrences doivent être supprimées
     * @param occurrences le nombre d'occurrences à supprimer
     * @return une nouvelle ImmutableBag avec les occurrences supprimées
     */
    ImmutableBag<T> newWithoutOccurrences(T element, int occurrences);

    @Override
    <S> ImmutableBag<S> selectInstancesOf(Class<S> clazz);

    @Override
    <V> ImmutableBag<V> collect(Function<? super T, ? extends V> function);

    @Override
    ImmutableBooleanBag collectBoolean(BooleanFunction<? super T> booleanFunction);

    @Override
    ImmutableByteBag collectByte(ByteFunction<? super T> byteFunction);

    @Override
    ImmutableCharBag collectChar(CharFunction<? super T> charFunction);

    @Override
    ImmutableDoubleBag collectDouble(DoubleFunction<? super T> doubleFunction);

    @Override
    ImmutableFloatBag collectFloat(FloatFunction<? super T> floatFunction);

    @Override
    ImmutableIntBag collectInt(IntFunction<? super T> intFunction);

    @Override
    ImmutableLongBag collectLong(LongFunction<? super T> longFunction);

    @Override
    ImmutableShortBag collectShort(ShortFunction<? super T> shortFunction);

    @Override
    <P, V> ImmutableBag<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    @Override
    <V> ImmutableBag<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    @Override
    <V> ImmutableBag<V> collectWithOccurrences(ObjectIntToObjectFunction<? super T, ? extends V> function);

    @Override
    <V> ImmutableBag<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    /**
     * @since 9.2
     */
    @Override
    default <P, V> ImmutableBag<V> flatCollectWith(Function2<? super T, ? super P, ? extends Iterable<V>> function, P parameter)
    {
        return this.flatCollect(each -> function.apply(each, parameter));
    }

    /**
     * @since 9.0
     */
    @Override
    default <V> ImmutableBag<V> countBy(Function<? super T, ? extends V> function)
    {
        return this.collect(function);
    }

    /**
     * @since 9.0
     */
    @Override
    default <V, P> ImmutableBag<V> countByWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.collectWith(function, parameter);
    }

    /**
     * @since 10.0.0
     */
    @Override
    default <V> ImmutableBag<V> countByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.flatCollect(function);
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Override
    @Deprecated
    <S> ImmutableBag<Pair<T, S>> zip(Iterable<S> that);

    /**
     * @since 6.0
     */
    @Override
    ImmutableList<ObjectIntPair<T>> topOccurrences(int count);

    /**
     * @since 6.0
     */
    @Override
    ImmutableList<ObjectIntPair<T>> bottomOccurrences(int count);

    /**
     * Overrides toImmutableBag in RichIterable to return this.
     *
     * @since 11.0
     */
    @Override
    default ImmutableBag<T> toImmutableBag()
    {
        return this;
    }
}
