package org.eclipse.collections.impl.list.mutable;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * Une classe utilitaire pour effectuer des opérations de modification sur les listes composites.
 *
 * @param <E> Le type des éléments de la liste
 */
public class CompositeListModifier<E>
{
    private final List<List<E>> lists;
    private final Runnable sizeUpdater;

    public CompositeListModifier(List<List<E>> lists, Runnable sizeUpdater)
    {
        this.lists = lists;
        this.sizeUpdater = sizeUpdater;
    }

    public boolean add(E element)
    {
        if (this.lists.isEmpty())
        {
            this.lists.add(FastList.newList());
        }
        List<E> lastList = this.lists.get(this.lists.size() - 1);
        boolean result = lastList.add(element);
        if (result)
        {
            this.sizeUpdater.run();
        }
        return result;
    }

    public boolean remove(Object element)
    {
        boolean removed = false;
        for (List<E> list : this.lists)
        {
            if (list.remove(element))
            {
                removed = true;
                break;
            }
        }
        if (removed)
        {
            this.sizeUpdater.run();
        }
        return removed;
    }

    public boolean addAll(Collection<? extends E> collection)
    {
        if (collection.isEmpty())
        {
            return false;
        }
        List<E> newList = collection instanceof List ? (List<E>) collection : FastList.newList(collection);
        this.lists.add(newList);
        this.sizeUpdater.run();
        return true;
    }

    public boolean removeAll(Collection<?> collection)
    {
        if (collection.isEmpty())
        {
            return false;
        }
        boolean changed = false;
        for (List<E> list : this.lists)
        {
            if (list.removeAll(collection))
            {
                changed = true;
            }
        }
        if (changed)
        {
            this.sizeUpdater.run();
        }
        return changed;
    }

    public boolean retainAll(Collection<?> collection)
    {
        boolean changed = false;
        for (List<E> list : this.lists)
        {
            if (list.retainAll(collection))
            {
                changed = true;
            }
        }
        if (changed)
        {
            this.sizeUpdater.run();
        }
        return changed;
    }

    public void clear()
    {
        this.lists.forEach(List::clear);
        this.sizeUpdater.run();
    }

    public E set(int index, E element)
    {
        int offset = 0;
        for (List<E> list : this.lists)
        {
            if (index < offset + list.size())
            {
                return list.set(index - offset, element);
            }
            offset += list.size();
        }
        throw new IndexOutOfBoundsException("Index: " + index);
    }

    public void add(int index, E element)
    {
        int offset = 0;
        for (List<E> list : this.lists)
        {
            if (index <= offset + list.size())
            {
                list.add(index - offset, element);
                this.sizeUpdater.run();
                return;
            }
            offset += list.size();
        }
        throw new IndexOutOfBoundsException("Index: " + index);
    }

    public E remove(int index)
    {
        int offset = 0;
        for (List<E> list : this.lists)
        {
            if (index < offset + list.size())
            {
                E removed = list.remove(index - offset);
                this.sizeUpdater.run();
                return removed;
            }
            offset += list.size();
        }
        throw new IndexOutOfBoundsException("Index: " + index);
    }

    public boolean removeIf(Predicate<? super E> filter)
    {
        boolean changed = false;
        for (List<E> list : this.lists)
        {
            if (list.removeIf(filter))
            {
                changed = true;
            }
        }
        if (changed)
        {
            this.sizeUpdater.run();
        }
        return changed;
    }
} 