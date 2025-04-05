/*
 * Copyright (c) 2024 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.list.mutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;

import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import org.eclipse.collections.api.block.function.primitive.FloatObjectToFloatFunction;
import org.eclipse.collections.api.block.function.primitive.IntObjectToIntFunction;
import org.eclipse.collections.api.block.function.primitive.LongObjectToLongFunction;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.block.procedure.Procedure2;
import org.eclipse.collections.api.block.procedure.primitive.ObjectIntProcedure;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.ParallelListIterable;
import org.eclipse.collections.impl.parallel.BatchIterable;
import org.eclipse.collections.impl.parallel.ParallelIterate;

/**
 * CompositeFastList se comporte comme une liste, mais est composée d'au moins une sous-liste.
 * Elle est utile lorsque vous ne voulez pas supporter le coût supplémentaire de l'ajout de plusieurs listes
 * ou de l'allocation de mémoire pour une super-liste à laquelle ajouter plusieurs sous-listes.
 * <p>
 * <b>Note:</b> Les opérations de mutation (par exemple add et remove, tri) modifieront les listes sous-jacentes -
 * assurez-vous donc de n'utiliser une liste composite que là où elle sera la seule référence aux sous-listes
 * (par exemple, une liste composite qui contient plusieurs résultats de requête est OK tant
 * qu'elle est la seule chose qui référence les listes)
 */
public final class CompositeFastList<E>
        extends AbstractCompositeList<E>
        implements BatchIterable<E>, Serializable
{
    private static final long serialVersionUID = 2L;
    private static final int DEFAULT_BATCH_SIZE = 1;

    private final CompositeListSearcher<E> searcher;
    private final CompositeListModifier<E> modifier;
    private final CompositeListIterator<E> iterator;
    private final CompositeListTransformer<E> transformer;

    public CompositeFastList()
    {
        super();
        this.searcher = new CompositeListSearcher<>(this.lists);
        this.modifier = new CompositeListModifier<>(this.lists, this::resetSize);
        this.iterator = new CompositeListIterator<>(this.lists, 0);
        this.transformer = new CompositeListTransformer<>(this.lists);
    }

    public CompositeFastList(int initialCapacity)
    {
        super(initialCapacity);
        this.searcher = new CompositeListSearcher<>(this.lists);
        this.modifier = new CompositeListModifier<>(this.lists, this::resetSize);
        this.iterator = new CompositeListIterator<>(this.lists, 0);
        this.transformer = new CompositeListTransformer<>(this.lists);
    }

    @Override
    public MutableList<E> clone()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".clone() not implemented yet");
    }

    @Override
    public void batchForEach(Procedure<? super E> procedure, int sectionIndex, int sectionCount)
    {
        if (this.lists.size() == 1)
        {
            this.lists.get(0).batchForEach(procedure, sectionIndex, sectionCount);
        }
        else
        {
            this.lists.get(sectionIndex).batchForEach(procedure, 0, DEFAULT_BATCH_SIZE);
        }
    }

    @Override
    public int getBatchCount(int batchSize)
    {
        if (this.lists.size() == 1)
        {
            return this.lists.get(0).getBatchCount(batchSize);
        }
        return this.lists.size();
    }

    @Override
    public CompositeFastList<E> reverseThis()
    {
        ParallelIterate.forEach(this.lists, FastList::reverseThis);
        this.lists.reverseThis();
        return this;
    }

    @Override
    public void each(Procedure<? super E> procedure)
    {
        this.lists.each(list -> list.forEach(procedure));
    }

    @Override
    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super E, ? extends IV> function)
    {
        return this.lists.injectInto(injectedValue, (inject, list) -> list.injectInto(inject, function));
    }

    @Override
    public int injectInto(int injectedValue, IntObjectToIntFunction<? super E> function)
    {
        return this.lists.injectInto(injectedValue, (inject, list) -> list.injectInto(inject, function));
    }

    @Override
    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super E> function)
    {
        return this.lists.injectInto(injectedValue, (inject, list) -> list.injectInto(inject, function));
    }

    @Override
    public long injectInto(long injectedValue, LongObjectToLongFunction<? super E> function)
    {
        return this.lists.injectInto(injectedValue, (inject, list) -> list.injectInto(inject, function));
    }

    @Override
    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super E> function)
    {
        return this.lists.injectInto(injectedValue, (inject, list) -> list.injectInto(inject, function));
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super E> objectIntProcedure)
    {
        this.lists.forEach(new ProcedureToInnerListObjectIntProcedure<>(objectIntProcedure));
    }

    @Override
    public void reverseForEach(Procedure<? super E> procedure)
    {
        this.lists.reverseForEach(each -> each.reverseForEach(procedure));
    }

    @Override
    public void reverseForEachWithIndex(ObjectIntProcedure<? super E> procedure)
    {
        this.lists.reverseForEach(new ProcedureToReverseInnerListObjectIntProcedure<>(procedure, this.size - 1));
    }

    @Override
    public <P> void forEachWith(Procedure2<? super E, ? super P> procedure2, P parameter)
    {
        this.lists.each(list -> list.forEachWith(procedure2, parameter));
    }

    @Override
    public boolean contains(Object object)
    {
        return this.searcher.contains(object);
    }

    @Override
    public Iterator<E> iterator()
    {
        return this.listIterator();
    }

    @Override
    public boolean containsAll(Collection<?> collection)
    {
        return this.searcher.containsAll(collection);
    }

    @Override
    public boolean add(E object)
    {
        return this.modifier.add(object);
    }

    @Override
    public boolean remove(Object object)
    {
        return this.modifier.remove(object);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection)
    {
        return this.modifier.addAll(collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection)
    {
        return this.modifier.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection)
    {
        return this.modifier.retainAll(collection);
    }

    @Override
    public void clear()
    {
        this.modifier.clear();
    }

    @Override
    public E get(int index)
    {
        this.rangeCheck(index);
        int offset = 0;
        for (List<E> list : this.lists)
        {
            if (index < offset + list.size())
            {
                return list.get(index - offset);
            }
            offset += list.size();
        }
        throw new IndexOutOfBoundsException("Index: " + index);
    }

    @Override
    public E set(int index, E element)
    {
        return this.modifier.set(index, element);
    }

    @Override
    public void add(int index, E element)
    {
        this.modifier.add(index, element);
    }

    @Override
    public E remove(int index)
    {
        return this.modifier.remove(index);
    }

    @Override
    public int indexOf(Object o)
    {
        return this.searcher.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o)
    {
        return this.searcher.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator()
    {
        return this.listIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index)
    {
        if (this.lists.size() > 1 || this.lists.isEmpty())
        {
            this.flatten();
        }
        return this.lists.get(0).listIterator(index);
    }

    @Override
    public int count(Predicate<? super E> predicate)
    {
        return this.searcher.count(predicate);
    }

    @Override
    public <P> int countWith(Predicate2<? super E, ? super P> predicate, P parameter)
    {
        int count = 0;
        for (List<E> list : this.lists)
        {
            count += list.countWith(predicate, parameter);
        }
        return count;
    }

    @Override
    public boolean anySatisfy(Predicate<? super E> predicate)
    {
        return this.searcher.anySatisfy(predicate);
    }

    @Override
    public <P> boolean anySatisfyWith(Predicate2<? super E, ? super P> predicate, P parameter)
    {
        return this.lists.anySatisfyWith((each, parm) -> each.anySatisfyWith(predicate, parm), parameter);
    }

    @Override
    public boolean allSatisfy(Predicate<? super E> predicate)
    {
        return this.searcher.allSatisfy(predicate);
    }

    @Override
    public <P> boolean allSatisfyWith(Predicate2<? super E, ? super P> predicate, P parameter)
    {
        return this.lists.allSatisfyWith((each, param) -> each.allSatisfyWith(predicate, param), parameter);
    }

    @Override
    public boolean noneSatisfy(Predicate<? super E> predicate)
    {
        return this.searcher.noneSatisfy(predicate);
    }

    @Override
    public <P> boolean noneSatisfyWith(Predicate2<? super E, ? super P> predicate, P parameter)
    {
        return this.lists.allSatisfyWith((each, param) -> each.noneSatisfyWith(predicate, param), parameter);
    }

    @Override
    public <R extends Collection<E>> R select(Predicate<? super E> predicate, R target)
    {
        return this.transformer.select(predicate, target);
    }

    @Override
    public <P, R extends Collection<E>> R selectWith(Predicate2<? super E, ? super P> predicate, P parameter, R target)
    {
        return this.transformer.selectWith(predicate, parameter, target);
    }

    @Override
    public <R extends Collection<E>> R reject(Predicate<? super E> predicate, R target)
    {
        return this.transformer.reject(predicate, target);
    }

    @Override
    public <P, R extends Collection<E>> R rejectWith(Predicate2<? super E, ? super P> predicate, P parameter, R target)
    {
        return this.transformer.rejectWith(predicate, parameter, target);
    }

    @Override
    public <V, R extends Collection<V>> R collect(Function<? super E, ? extends V> function, R target)
    {
        return this.transformer.collect(function, target);
    }

    @Override
    public <P, A, R extends Collection<A>> R collectWith(Function2<? super E, ? super P, ? extends A> function, P parameter, R target)
    {
        return this.transformer.collectWith(function, parameter, target);
    }

    @Override
    public <V, R extends Collection<V>> R flatCollect(Function<? super E, ? extends Iterable<V>> function, R target)
    {
        return this.transformer.flatCollect(function, target);
    }

    @Override
    public <V, R extends Collection<V>> R flatCollectWith(Function2<? super E, ? super P, ? extends Iterable<V>> function, P parameter, R target)
    {
        return this.transformer.flatCollectWith(function, parameter, target);
    }

    @Override
    public void sort(Comparator<? super E> comparator)
    {
        FastList<E> list = comparator == null
                ? (FastList<E>) this.toSortedList()
                : (FastList<E>) this.toSortedList(comparator);
        this.lists.clear();
        this.lists.add(list);
    }

    @Override
    public ParallelListIterable<E> asParallel(ExecutorService executorService, int batchSize)
    {
        return new NonParallelListIterable<>(this);
    }

    private static final class ProcedureToInnerListObjectIntProcedure<E> implements Procedure<FastList<E>>
    {
        private static final long serialVersionUID = 1L;

        private int index;
        private final ObjectIntProcedure<? super E> objectIntProcedure;

        private ProcedureToInnerListObjectIntProcedure(ObjectIntProcedure<? super E> objectIntProcedure)
        {
            this.objectIntProcedure = objectIntProcedure;
        }

        @Override
        public void value(FastList<E> list)
        {
            list.each(object ->
            {
                this.objectIntProcedure.value(
                        object,
                        this.index);
                this.index++;
            });
        }
    }

    private static final class ProcedureToReverseInnerListObjectIntProcedure<E> implements Procedure<FastList<E>>
    {
        private static final long serialVersionUID = 1L;

        private int index;
        private final ObjectIntProcedure<? super E> objectIntProcedure;

        private ProcedureToReverseInnerListObjectIntProcedure(ObjectIntProcedure<? super E> objectIntProcedure, int size)
        {
            this.objectIntProcedure = objectIntProcedure;
            this.index = size;
        }

        @Override
        public void value(FastList<E> list)
        {
            list.reverseForEach(object ->
            {
                this.objectIntProcedure.value(
                        object,
                        this.index);
                this.index--;
            });
        }
    }
}
