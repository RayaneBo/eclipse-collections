package org.eclipse.collections.impl.list.mutable;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Un itérateur pour les listes composites qui permet de parcourir les éléments
 * de toutes les sous-listes de manière transparente.
 *
 * @param <E> Le type des éléments de la liste
 */
public class CompositeListIterator<E> implements ListIterator<E>
{
    private final List<List<E>> lists;
    private int currentListIndex;
    private ListIterator<E> currentIterator;
    private int currentIndex;
    private int lastReturnedIndex = -1;

    public CompositeListIterator(List<List<E>> lists, int index)
    {
        this.lists = lists;
        this.currentListIndex = 0;
        this.currentIndex = index;
        this.initializeCurrentIterator();
    }

    private void initializeCurrentIterator()
    {
        if (this.lists.isEmpty())
        {
            this.currentIterator = null;
            return;
        }

        int remainingIndex = this.currentIndex;
        while (this.currentListIndex < this.lists.size())
        {
            List<E> currentList = this.lists.get(this.currentListIndex);
            if (remainingIndex < currentList.size())
            {
                this.currentIterator = currentList.listIterator(remainingIndex);
                return;
            }
            remainingIndex -= currentList.size();
            this.currentListIndex++;
        }
        this.currentIterator = null;
    }

    @Override
    public boolean hasNext()
    {
        if (this.currentIterator == null)
        {
            return false;
        }
        if (this.currentIterator.hasNext())
        {
            return true;
        }
        return this.currentListIndex < this.lists.size() - 1;
    }

    @Override
    public E next()
    {
        if (!this.hasNext())
        {
            throw new NoSuchElementException();
        }
        if (!this.currentIterator.hasNext())
        {
            this.currentListIndex++;
            List<E> nextList = this.lists.get(this.currentListIndex);
            this.currentIterator = nextList.listIterator();
        }
        this.lastReturnedIndex = this.currentIndex;
        this.currentIndex++;
        return this.currentIterator.next();
    }

    @Override
    public boolean hasPrevious()
    {
        if (this.currentIterator == null)
        {
            return false;
        }
        if (this.currentIterator.hasPrevious())
        {
            return true;
        }
        return this.currentListIndex > 0;
    }

    @Override
    public E previous()
    {
        if (!this.hasPrevious())
        {
            throw new NoSuchElementException();
        }
        if (!this.currentIterator.hasPrevious())
        {
            this.currentListIndex--;
            List<E> previousList = this.lists.get(this.currentListIndex);
            this.currentIterator = previousList.listIterator(previousList.size());
        }
        this.lastReturnedIndex = this.currentIndex - 1;
        this.currentIndex--;
        return this.currentIterator.previous();
    }

    @Override
    public int nextIndex()
    {
        return this.currentIndex;
    }

    @Override
    public int previousIndex()
    {
        return this.currentIndex - 1;
    }

    @Override
    public void remove()
    {
        if (this.lastReturnedIndex == -1)
        {
            throw new IllegalStateException();
        }
        this.currentIterator.remove();
        this.lastReturnedIndex = -1;
    }

    @Override
    public void set(E e)
    {
        if (this.lastReturnedIndex == -1)
        {
            throw new IllegalStateException();
        }
        this.currentIterator.set(e);
    }

    @Override
    public void add(E e)
    {
        this.currentIterator.add(e);
        this.lastReturnedIndex = -1;
        this.currentIndex++;
    }
} 