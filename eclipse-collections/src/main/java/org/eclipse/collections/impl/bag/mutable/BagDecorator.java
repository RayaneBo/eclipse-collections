package org.eclipse.collections.impl.bag.mutable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.collections.api.bag.Bag;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.function.primitive.ObjectIntToObjectFunction;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.block.predicate.primitive.IntPredicate;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.multimap.bag.MutableBagMultimap;
import org.eclipse.collections.api.partition.bag.PartitionMutableBag;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.collection.mutable.AbstractCollectionAdapter;

/**
 * Une classe de base pour les décorateurs de Bag qui ajoutent des fonctionnalités optionnelles.
 * Cette classe implémente le pattern Decorator pour permettre l'ajout de fonctionnalités
 * sans modifier la classe de base.
 *
 * @param <T> le type des éléments dans la bag
 */
public abstract class BagDecorator<T>
        extends AbstractCollectionAdapter<T>
        implements MutableBag<T>
{
    private static final long serialVersionUID = 1L;

    protected final MutableBag<T> delegate;

    protected BagDecorator(MutableBag<T> delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public MutableBag<T> asUnmodifiable()
    {
        return this.delegate.asUnmodifiable();
    }

    @Override
    public MutableBag<T> asSynchronized()
    {
        return this.delegate.asSynchronized();
    }

    @Override
    public MutableBag<T> select(Predicate<? super T> predicate)
    {
        return this.delegate.select(predicate);
    }

    @Override
    public <P> MutableBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.delegate.selectWith(predicate, parameter);
    }

    @Override
    public MutableBag<T> reject(Predicate<? super T> predicate)
    {
        return this.delegate.reject(predicate);
    }

    @Override
    public <P> MutableBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.delegate.rejectWith(predicate, parameter);
    }

    @Override
    public PartitionMutableBag<T> partition(Predicate<? super T> predicate)
    {
        return this.delegate.partition(predicate);
    }

    @Override
    public <P> PartitionMutableBag<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.delegate.partitionWith(predicate, parameter);
    }

    @Override
    public <S> MutableBag<S> selectInstancesOf(Class<S> clazz)
    {
        return this.delegate.selectInstancesOf(clazz);
    }

    @Override
    public <V> MutableBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.delegate.groupBy(function);
    }

    @Override
    public <V> MutableBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.delegate.groupByEach(function);
    }

    @Override
    public MutableSet<Pair<T, Integer>> zipWithIndex()
    {
        return this.delegate.zipWithIndex();
    }

    @Override
    public MutableBag<T> tap(Procedure<? super T> procedure)
    {
        this.delegate.forEach(procedure);
        return this;
    }

    @Override
    public void forEachWithOccurrences(ObjectIntProcedure<? super T> procedure)
    {
        this.delegate.forEachWithOccurrences(procedure);
    }

    @Override
    public int occurrencesOf(Object item)
    {
        return this.delegate.occurrencesOf(item);
    }

    @Override
    public MutableMap<T, Integer> toMapOfItemToCount()
    {
        return this.delegate.toMapOfItemToCount();
    }

    @Override
    public MutableBag<T> selectByOccurrences(IntPredicate predicate)
    {
        return this.delegate.selectByOccurrences(predicate);
    }

    @Override
    public MutableSet<T> selectUnique()
    {
        return this.delegate.selectUnique();
    }

    @Override
    public boolean addOccurrences(T item, int occurrences)
    {
        return this.delegate.addOccurrences(item, occurrences);
    }

    @Override
    public boolean removeOccurrences(Object item, int occurrences)
    {
        return this.delegate.removeOccurrences(item, occurrences);
    }

    @Override
    public boolean setOccurrences(T item, int count)
    {
        return this.delegate.setOccurrences(item, count);
    }

    @Override
    public MutableBag<T> newEmpty()
    {
        return this.delegate.newEmpty();
    }

    @Override
    public boolean equals(Object otherBag)
    {
        return this.delegate.equals(otherBag);
    }

    @Override
    public int hashCode()
    {
        return this.delegate.hashCode();
    }

    @Override
    public String toString()
    {
        return this.delegate.toString();
    }

    @Override
    public Iterator<T> iterator()
    {
        return this.delegate.iterator();
    }

    @Override
    public int size()
    {
        return this.delegate.size();
    }

    @Override
    public boolean isEmpty()
    {
        return this.delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o)
    {
        return this.delegate.contains(o);
    }

    @Override
    public Object[] toArray()
    {
        return this.delegate.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        return this.delegate.toArray(a);
    }

    @Override
    public boolean add(T t)
    {
        return this.delegate.add(t);
    }

    @Override
    public boolean remove(Object o)
    {
        return this.delegate.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        return this.delegate.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        return this.delegate.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        return this.delegate.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        return this.delegate.retainAll(c);
    }

    @Override
    public void clear()
    {
        this.delegate.clear();
    }

    @Override
    public Set<T> toSet()
    {
        return this.delegate.toSet();
    }

    @Override
    public MutableBag<T> with(T element)
    {
        return this.delegate.with(element);
    }

    @Override
    public MutableBag<T> without(T element)
    {
        return this.delegate.without(element);
    }

    @Override
    public MutableBag<T> withAll(Iterable<? extends T> elements)
    {
        return this.delegate.withAll(elements);
    }

    @Override
    public MutableBag<T> withoutAll(Iterable<? extends T> elements)
    {
        return this.delegate.withoutAll(elements);
    }

    @Override
    public MutableBag<T> withOccurrences(T element, int occurrences)
    {
        return this.delegate.withOccurrences(element, occurrences);
    }

    @Override
    public MutableBag<T> withoutOccurrences(T element, int occurrences)
    {
        return this.delegate.withoutOccurrences(element, occurrences);
    }
} 