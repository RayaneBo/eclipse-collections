package org.eclipse.collections.impl.parallel;

import java.io.Serializable;

import org.eclipse.collections.api.parallel.CombinationStrategy;

/**
 * Une classe abstraite de base pour les stratégies de combinaison.
 * Fournit une implémentation par défaut des méthodes communes.
 *
 * @param <T> Le type des éléments à combiner
 * @param <R> Le type du résultat de la combinaison
 */
public abstract class AbstractCombinationStrategy<T, R> implements CombinationStrategy<T, R>, Serializable
{
    private static final long serialVersionUID = 1L;
    private final boolean useCombineOne;

    protected AbstractCombinationStrategy(boolean useCombineOne)
    {
        this.useCombineOne = useCombineOne;
    }

    @Override
    public boolean useCombineOne()
    {
        return this.useCombineOne;
    }
} 