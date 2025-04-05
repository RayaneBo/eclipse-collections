package org.eclipse.collections.impl.parallel;

import java.util.Collection;

import org.eclipse.collections.api.parallel.CombinationStrategy;
import org.eclipse.collections.impl.list.mutable.CompositeFastList;

/**
 * Une stratégie de combinaison pour les collections.
 * Cette stratégie combine les éléments en les ajoutant à une collection cible.
 *
 * @param <T> Le type des éléments à combiner
 */
public class CollectionCombinationStrategy<T> extends AbstractCombinationStrategy<Collection<T>, Collection<T>>
{
    private static final long serialVersionUID = 1L;
    private final Collection<T> targetCollection;

    public CollectionCombinationStrategy(Collection<T> targetCollection, boolean useCombineOne)
    {
        super(useCombineOne);
        this.targetCollection = targetCollection;
    }

    @Override
    public Collection<T> combineOne(Collection<T> element, Collection<T> result)
    {
        result.addAll(element);
        return result;
    }

    @Override
    public Collection<T> getInitialValue()
    {
        return this.targetCollection != null ? this.targetCollection : new CompositeFastList<>();
    }
} 