package org.eclipse.collections.impl.parallel;

import org.eclipse.collections.api.parallel.CombinationStrategy;

/**
 * Une stratégie de combinaison qui ne fait rien.
 * Cette stratégie est utilisée pour les opérations qui ne nécessitent pas de combinaison.
 *
 * @param <T> Le type des éléments
 */
public final class PassThruCombinationStrategy<T> extends AbstractCombinationStrategy<T, Void>
{
    private static final long serialVersionUID = 1L;

    public PassThruCombinationStrategy()
    {
        super(true);
    }

    @Override
    public Void combineOne(T element, Void result)
    {
        return null;
    }

    @Override
    public Void getInitialValue()
    {
        return null;
    }
} 