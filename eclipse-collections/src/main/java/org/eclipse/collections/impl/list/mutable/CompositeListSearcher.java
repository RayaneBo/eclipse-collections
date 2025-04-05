package org.eclipse.collections.impl.list.mutable;

import java.util.List;
import java.util.function.Predicate;

/**
 * Une classe utilitaire pour effectuer des opérations de recherche sur les listes composites.
 *
 * @param <E> Le type des éléments de la liste
 */
public class CompositeListSearcher<E>
{
    private final List<List<E>> lists;

    public CompositeListSearcher(List<List<E>> lists)
    {
        this.lists = lists;
    }

    public int indexOf(Object o)
    {
        int offset = 0;
        for (List<E> list : this.lists)
        {
            int index = list.indexOf(o);
            if (index != -1)
            {
                return offset + index;
            }
            offset += list.size();
        }
        return -1;
    }

    public int lastIndexOf(Object o)
    {
        int offset = 0;
        for (int i = this.lists.size() - 1; i >= 0; i--)
        {
            List<E> list = this.lists.get(i);
            int index = list.lastIndexOf(o);
            if (index != -1)
            {
                return offset + index;
            }
            offset += list.size();
        }
        return -1;
    }

    public boolean contains(Object o)
    {
        return this.lists.stream().anyMatch(list -> list.contains(o));
    }

    public boolean containsAll(List<?> c)
    {
        return c.stream().allMatch(this::contains);
    }

    public int count(Predicate<? super E> predicate)
    {
        return this.lists.stream()
                .mapToInt(list -> (int) list.stream().filter(predicate).count())
                .sum();
    }

    public boolean anySatisfy(Predicate<? super E> predicate)
    {
        return this.lists.stream().anyMatch(list -> list.stream().anyMatch(predicate));
    }

    public boolean allSatisfy(Predicate<? super E> predicate)
    {
        return this.lists.stream().allMatch(list -> list.stream().allMatch(predicate));
    }

    public boolean noneSatisfy(Predicate<? super E> predicate)
    {
        return this.lists.stream().allMatch(list -> list.stream().noneMatch(predicate));
    }
} 