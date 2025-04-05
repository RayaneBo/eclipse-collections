package org.eclipse.collections.impl.list.mutable;

import java.util.List;
import java.util.function.UnaryOperator;

import org.eclipse.collections.api.list.CompositeList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.FastList;

/**
 * Une classe abstraite qui implémente les fonctionnalités de base d'une liste composite.
 * Cette classe fournit une implémentation par défaut des méthodes communes.
 *
 * @param <E> Le type des éléments de la liste
 */
public abstract class AbstractCompositeList<E> implements CompositeList<E>
{
    protected final MutableList<MutableList<E>> lists;
    protected int size;

    protected AbstractCompositeList()
    {
        this.lists = FastList.newList();
        this.size = 0;
    }

    protected AbstractCompositeList(int initialCapacity)
    {
        if (initialCapacity < 0)
        {
            throw new IllegalArgumentException("Initial capacity cannot be negative");
        }
        this.lists = FastList.newList(Math.max(initialCapacity, 1));
        this.size = 0;
    }

    @Override
    public boolean addComposited(List<E> list)
    {
        if (list == null)
        {
            throw new IllegalArgumentException("Cannot add null list");
        }
        MutableList<E> mutableList = list instanceof MutableList ? (MutableList<E>) list : FastList.newList(list);
        this.lists.add(mutableList);
        this.size += mutableList.size();
        return true;
    }

    @Override
    public int getCompositeCount()
    {
        return this.lists.size();
    }

    @Override
    public List<E> getComposite(int index)
    {
        if (index < 0 || index >= this.lists.size())
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.lists.size());
        }
        return this.lists.get(index);
    }

    @Override
    public void flatten()
    {
        if (this.lists.size() <= 1)
        {
            return;
        }
        MutableList<E> flattened = FastList.newList(this.size);
        this.lists.forEach(flattened::addAll);
        this.lists.clear();
        this.lists.add(flattened);
    }

    @Override
    public int size()
    {
        return this.size;
    }

    @Override
    public boolean isEmpty()
    {
        return this.size == 0;
    }

    @Override
    public void clear()
    {
        this.lists.clear();
        this.size = 0;
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator)
    {
        this.lists.forEach(list -> list.replaceAll(operator));
    }

    protected void resetSize()
    {
        this.size = this.lists.sumOfInt(List::size);
    }

    protected void rangeCheck(int index)
    {
        if (index < 0 || index >= this.size)
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
        }
    }
} 