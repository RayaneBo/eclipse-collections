/*
 * Copyright (c) 2024 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.api.bag;

import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function0;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.function.primitive.DoubleFunction;
import org.eclipse.collections.api.block.function.primitive.FloatFunction;
import org.eclipse.collections.api.block.function.primitive.IntFunction;
import org.eclipse.collections.api.block.function.primitive.LongFunction;
import org.eclipse.collections.api.block.function.primitive.ObjectIntToObjectFunction;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.block.predicate.primitive.IntPredicate;
import org.eclipse.collections.api.block.predicate.primitive.ObjectIntPredicate;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.block.procedure.primitive.ObjectIntProcedure;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.api.map.MutableMapIterable;
import org.eclipse.collections.api.multimap.bag.BagMultimap;
import org.eclipse.collections.api.partition.bag.PartitionBag;
import org.eclipse.collections.api.set.SetIterable;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.api.tuple.primitive.ObjectIntPair;

/**
 * Une Bag est une Collection dont les éléments sont non ordonnés et peuvent contenir des entrées en double.
 * Elle diffère de MutableCollection en ce qu'elle ajoute un protocole pour déterminer, ajouter et supprimer
 * le nombre d'occurrences d'un élément.
 *
 * @since 1.0
 */
public interface Bag<T>
        extends RichIterable<T>
{
    /**
     * Deux bags {@code b1} et {@code b2} sont égaux si {@code m1.toMapOfItemToCount().equals(m2.toMapOfItemToCount())}.
     *
     * @param otherBag l'objet à comparer avec ce bag
     * @return true si les bags sont égaux, false sinon
     * @see Map#equals(Object)
     */
    @Override
    boolean equals(Object otherBag);

    /**
     * Retourne le code de hachage pour ce Bag, défini comme <em>this.{@link #toMapOfItemToCount()}.hashCode()</em>.
     *
     * @return le code de hachage pour ce bag
     * @see Map#hashCode()
     */
    @Override
    int hashCode();

    /**
     * Pour chaque élément distinct, avec son nombre d'occurrences, exécute la procédure spécifiée.
     *
     * @param procedure la procédure à exécuter pour chaque élément distinct et son compte
     */
    void forEachWithOccurrences(ObjectIntProcedure<? super T> procedure);

    /**
     * Retourne le nombre d'occurrences d'un élément.
     *
     * @param item l'élément à compter
     * @return le nombre d'occurrences de l'élément
     */
    int occurrencesOf(Object item);

    /**
     * Retourne une map avec le type d'élément à son compte comme un Integer.
     *
     * @return une map des éléments à leurs comptes
     */
    MapIterable<T, Integer> toMapOfItemToCount();

    /**
     * Retourne true si le prédicat s'évalue à true pour n'importe quel élément du Bag.
     * Retourne false si le Bag est vide ou si aucun élément ne retourne true pour le prédicat.
     *
     * @param predicate le prédicat à évaluer
     * @return true si un élément satisfait le prédicat, false sinon
     * @since 11.0
     */
    default boolean anySatisfyWithOccurrences(ObjectIntPredicate<? super T> predicate)
    {
        return this.detectWithOccurrences(predicate) != null;
    }

    /**
     * Retourne true si le prédicat s'évalue à true pour tous les éléments du Bag.
     * Retourne false si le Bag est vide ou si tous les éléments ne retournent pas true pour le prédicat.
     *
     * @param predicate le prédicat à évaluer
     * @return true si tous les éléments satisfont le prédicat, false sinon
     * @since 11.0
     */
    default boolean allSatisfyWithOccurrences(ObjectIntPredicate<? super T> predicate)
    {
        return !this.anySatisfyWithOccurrences((each, occurrences) -> !predicate.accept(each, occurrences));
    }

    /**
     * Retourne true si le Bag est vide ou si le prédicat s'évalue à false pour tous les éléments du Bag.
     * Retourne false si le prédicat s'évalue à true pour au moins un élément du Bag.
     *
     * @param predicate le prédicat à évaluer
     * @return true si aucun élément ne satisfait le prédicat, false sinon
     * @since 11.0
     */
    default boolean noneSatisfyWithOccurrences(ObjectIntPredicate<? super T> predicate)
    {
        return !this.anySatisfyWithOccurrences(predicate);
    }

    /**
     * Retourne un élément du Bag qui satisfait le prédicat ou null si un tel élément n'existe pas.
     *
     * @param predicate le prédicat à évaluer
     * @return un élément qui satisfait le prédicat, ou null si aucun n'existe
     * @since 11.0
     */
    T detectWithOccurrences(ObjectIntPredicate<? super T> predicate);

    /**
     * Retourne tous les éléments du bag qui ont un nombre d'occurrences qui satisfait le prédicat.
     *
     * @param predicate le prédicat à évaluer pour chaque occurrence d'élément
     * @return un nouveau bag contenant les éléments qui satisfont le prédicat
     * @since 3.0
     */
    Bag<T> selectByOccurrences(IntPredicate predicate);

    /**
     * Retourne tous les éléments du bag qui ont plus d'une occurrence.
     *
     * @return un nouveau bag contenant les éléments en double
     * @since 9.2
     */
    default Bag<T> selectDuplicates()
    {
        return this.selectByOccurrences(occurrences -> occurrences > 1);
    }

    /**
     * Retourne un ensemble contenant tous les éléments du bag qui ont exactement une occurrence.
     *
     * @return un ensemble d'éléments uniques
     * @since 9.2
     */
    SetIterable<T> selectUnique();

    /**
     * Retourne les {@code count} éléments les plus fréquemment présents.
     *
     * En cas d'égalité, tous les éléments dont le nombre d'occurrences correspond aux occurrences du dernier
     * élément seront retournés.
     *
     * @param count le nombre d'occurrences supérieures à retourner
     * @return une liste des éléments les plus fréquemment présents et leurs comptes
     * @since 6.0
     */
    ListIterable<ObjectIntPair<T>> topOccurrences(int count);

    /**
     * Retourne les {@code count} éléments les moins fréquemment présents.
     *
     * En cas d'égalité, tous les éléments dont le nombre d'occurrences correspond aux occurrences du dernier
     * élément seront retournés.
     *
     * @param count le nombre d'occurrences inférieures à retourner
     * @return une liste des éléments les moins fréquemment présents et leurs comptes
     * @since 6.0
     */
    ListIterable<ObjectIntPair<T>> bottomOccurrences(int count);

    /**
     * The size of the Bag when counting only distinct elements.
     *
     * @return the number of distinct elements in the bag
     */
    int sizeDistinct();

    /**
     * Returns a string representation of this bag. The string representation consists of a list of element-count mappings.
     *
     * <pre>
     * Assert.assertEquals("{1=1, 2=2, 3=3}", Bags.mutable.with(1, 2, 2, 3, 3, 3).toStringOfItemToCount());
     * </pre>
     * This string representation is similar to {@link java.util.AbstractMap#toString()}, not {@link RichIterable#toString()},
     * whereas the {@code toString()} implementation for a Bag is consistent with {@link RichIterable#toString()}.
     *
     * @return a string representation of this bag
     * @since 3.0
     */
    String toStringOfItemToCount();

    /**
     * Returns an immutable copy of this bag.
     *
     * @return an immutable bag containing the same elements as this bag
     */
    ImmutableBagIterable<T> toImmutable();

    /**
     * Returns summary statistics for the int values produced by applying the function to each element.
     *
     * @param function the function to apply to each element
     * @return summary statistics for the int values
     * @since 8.0
     */
    @Override
    default IntSummaryStatistics summarizeInt(IntFunction<? super T> function)
    {
        IntSummaryStatistics stats = new IntSummaryStatistics();
        this.forEachWithOccurrences((each, occurrences) ->
        {
            int result = function.intValueOf(each);
            for (int i = 0; i < occurrences; i++)
            {
                stats.accept(result);
            }
        });
        return stats;
    }

    /**
     * Returns summary statistics for the float values produced by applying the function to each element.
     *
     * @param function the function to apply to each element
     * @return summary statistics for the float values
     * @since 8.0
     */
    @Override
    default DoubleSummaryStatistics summarizeFloat(FloatFunction<? super T> function)
    {
        DoubleSummaryStatistics stats = new DoubleSummaryStatistics();
        this.forEachWithOccurrences((each, occurrences) ->
        {
            float result = function.floatValueOf(each);
            for (int i = 0; i < occurrences; i++)
            {
                stats.accept(result);
            }
        });
        return stats;
    }

    /**
     * Returns summary statistics for the long values produced by applying the function to each element.
     *
     * @param function the function to apply to each element
     * @return summary statistics for the long values
     * @since 8.0
     */
    @Override
    default LongSummaryStatistics summarizeLong(LongFunction<? super T> function)
    {
        LongSummaryStatistics stats = new LongSummaryStatistics();
        this.forEachWithOccurrences((each, occurrences) ->
        {
            long result = function.longValueOf(each);
            for (int i = 0; i < occurrences; i++)
            {
                stats.accept(result);
            }
        });
        return stats;
    }

    /**
     * Returns summary statistics for the double values produced by applying the function to each element.
     *
     * @param function the function to apply to each element
     * @return summary statistics for the double values
     * @since 8.0
     */
    @Override
    default DoubleSummaryStatistics summarizeDouble(DoubleFunction<? super T> function)
    {
        DoubleSummaryStatistics stats = new DoubleSummaryStatistics();
        this.forEachWithOccurrences((each, occurrences) ->
        {
            double result = function.doubleValueOf(each);
            for (int i = 0; i < occurrences; i++)
            {
                stats.accept(result);
            }
        });
        return stats;
    }

    /**
     * This method produces the equivalent result as {@link Stream#collect(Collector)}.
     *
     * @param collector the collector to use
     * @param <R> the type of the result
     * @param <A> the intermediate accumulation type of the collector
     * @return the result of the collection
     * @since 8.0
     */
    @Override
    default <R, A> R reduceInPlace(Collector<? super T, A, R> collector)
    {
        A mutableResult = collector.supplier().get();
        BiConsumer<A, ? super T> accumulator = collector.accumulator();
        this.forEachWithOccurrences((each, occurrences) ->
        {
            for (int i = 0; i < occurrences; i++)
            {
                accumulator.accept(mutableResult, each);
            }
        });
        return collector.finisher().apply(mutableResult);
    }

    /**
     * This method produces the equivalent result as {@link Stream#collect(Supplier, BiConsumer, BiConsumer)}.
     *
     * @param supplier the supplier to create the result container
     * @param accumulator the accumulator to add elements to the result
     * @param <R> the type of the result
     * @return the result of the collection
     * @since 8.0
     */
    @Override
    default <R> R reduceInPlace(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator)
    {
        R mutableResult = supplier.get();
        this.forEachWithOccurrences((each, occurrences) ->
        {
            for (int i = 0; i < occurrences; i++)
            {
                accumulator.accept(mutableResult, each);
            }
        });
        return mutableResult;
    }

    /**
     * Iterates over the unique elements and their occurrences and collects the results of applying the specified function.
     *
     * @param function the function to apply to each element and its occurrences
     * @param <V> the type of the result
     * @return a rich iterable of the results
     * @since 10.0
     */
    <V> RichIterable<V> collectWithOccurrences(ObjectIntToObjectFunction<? super T, ? extends V> function);

    /**
     * Iterates over the unique elements and their occurrences and collects the results of applying the
     * specified function into the target collection.
     *
     * @param function the function to apply to each element and its occurrences
     * @param target the target collection to add results to
     * @param <V> the type of the result
     * @param <R> the type of the target collection
     * @return the target collection with the results added
     * @since 9.1
     */
    default <V, R extends Collection<V>> R collectWithOccurrences(
            ObjectIntToObjectFunction<? super T, ? extends V> function,
            R target)
    {
        this.forEachWithOccurrences((each, occurrences) -> target.add(function.valueOf(each, occurrences)));
        return target;
    }

    /**
     * Applies an aggregate function over the iterable grouping results into the target map based on the specific
     * groupBy function. Aggregate results are allowed to be immutable as they will be replaced in place in the map. A
     * second function specifies the initial "zero" aggregate value to work with (i.e. Integer.valueOf(0)).
     *
     * This method is overridden and optimized for Bag to use forEachWithOccurrences instead of forEach.
     *
     * @param groupBy the function to group elements by
     * @param zeroValueFactory the function to create initial values
     * @param nonMutatingAggregator the function to aggregate values
     * @param target the target map to store results in
     * @param <K> the type of the keys
     * @param <V> the type of the values
     * @param <R> the type of the target map
     * @return the target map with the aggregated results
     * @since 10.3
     */
    @Override
    default <K, V, R extends MutableMapIterable<K, V>> R aggregateBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Function2<? super V, ? super T, ? extends V> nonMutatingAggregator,
            R target)
    {
        this.forEachWithOccurrences((each, occurrences) ->
        {
            K key = groupBy.valueOf(each);
            for (int i = 0; i < occurrences; i++)
            {
                target.updateValueWith(key, zeroValueFactory, nonMutatingAggregator, each);
            }
        });
        return target;
    }

    /**
     * Reduces the elements of this bag by grouping them according to the specified function and applying the reduce function.
     *
     * @param groupBy the function to group elements by
     * @param reduceFunction the function to reduce values
     * @param target the target map to store results in
     * @param <K> the type of the keys
     * @param <R> the type of the target map
     * @return the target map with the reduced results
     */
    @Override
    default <K, R extends MutableMapIterable<K, T>> R reduceBy(
            Function<? super T, ? extends K> groupBy,
            Function2<? super T, ? super T, ? extends T> reduceFunction,
            R target)
    {
        this.forEachWithOccurrences((each, occurrences) ->
        {
            K key = groupBy.valueOf(each);
            for (int i = 0; i < occurrences; i++)
            {
                target.merge(key, each, reduceFunction);
            }
        });
        return target;
    }
}
