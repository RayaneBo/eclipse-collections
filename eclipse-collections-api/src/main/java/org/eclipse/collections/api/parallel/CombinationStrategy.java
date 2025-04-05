package org.eclipse.collections.api.parallel;

import java.io.Serializable;

/**
 * Une interface représentant une stratégie de combinaison pour fusionner les résultats de procédures parallèles.
 * Cette interface permet de définir différentes stratégies de combinaison selon le type de résultat attendu.
 *
 * @param <T> Le type des éléments à combiner
 * @param <R> Le type du résultat de la combinaison
 */
public interface CombinationStrategy<T, R> extends Serializable
{
    /**
     * Combine un élément avec le résultat actuel.
     *
     * @param element L'élément à combiner
     * @param result Le résultat actuel
     * @return Le nouveau résultat après combinaison
     */
    R combineOne(T element, R result);

    /**
     * Retourne la valeur initiale pour le résultat.
     *
     * @return La valeur initiale
     */
    R getInitialValue();

    /**
     * Vérifie si la stratégie nécessite une combinaison un par un.
     *
     * @return true si la combinaison doit être faite un par un, false sinon
     */
    boolean useCombineOne();
} 