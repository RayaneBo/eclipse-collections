package org.eclipse.collections.impl.list.mutable;

import java.util.Collection;
import java.util.List;

import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.Predicate2;

/**
 * Une classe utilitaire pour effectuer des opérations de transformation sur les listes composites.
 *
 * @param <E> Le type des éléments de la liste
 */
public class CompositeListTransformer<E>
{
    private final List<List<E>> lists;

    public CompositeListTransformer(List<List<E>> lists)
    {
        this.lists = lists;
    }

    public <R extends Collection<E>> R select(Predicate<? super E> predicate, R target)
    {
        for (List<E> list : this.lists)
        {
            list.select(predicate, target);
        }
        return target;
    }

    public <P, R extends Collection<E>> R selectWith(Predicate2<? super E, ? super P> predicate, P parameter, R target)
    {
        for (List<E> list : this.lists)
        {
            list.selectWith(predicate, parameter, target);
        }
        return target;
    }

    public <R extends Collection<E>> R reject(Predicate<? super E> predicate, R target)
    {
        for (List<E> list : this.lists)
        {
            list.reject(predicate, target);
        }
        return target;
    }

    public <P, R extends Collection<E>> R rejectWith(Predicate2<? super E, ? super P> predicate, P parameter, R target)
    {
        for (List<E> list : this.lists)
        {
            list.rejectWith(predicate, parameter, target);
        }
        return target;
    }

    public <V, R extends Collection<E>> R collect(Function<? super E, ? extends V> function, R target)
    {
        for (List<E> list : this.lists)
        {
            list.collect(function, target);
        }
        return target;
    }

    public <P, A, R extends Collection<A>> R collectWith(Function2<? super E, ? super P, ? extends A> function, P parameter, R target)
    {
        for (List<E> list : this.lists)
        {
            list.collectWith(function, parameter, target);
        }
        return target;
    }

    public <V, R extends Collection<V>> R flatCollect(Function<? super E, ? extends Iterable<V>> function, R target)
    {
        for (List<E> list : this.lists)
        {
            list.flatCollect(function, target);
        }
        return target;
    }

    public <V, R extends Collection<V>> R flatCollectWith(Function2<? super E, ? super P, ? extends Iterable<V>> function, P parameter, R target)
    {
        for (List<E> list : this.lists)
        {
            list.flatCollectWith(function, parameter, target);
        }
        return target;
    }
} 