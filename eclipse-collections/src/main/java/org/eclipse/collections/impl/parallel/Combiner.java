/*
 * Copyright (c) 2021 Goldman Sachs.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.parallel;

import java.io.Serializable;
import java.util.Objects;

import org.eclipse.collections.api.parallel.CombinationStrategy;
import org.eclipse.collections.impl.utility.Iterate;

/**
 * Une classe qui combine les résultats de procédures parallèles en utilisant une stratégie de combinaison.
 *
 * @param <T> Le type des éléments à combiner
 * @param <R> Le type du résultat de la combinaison
 */
public class Combiner<T, R> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final CombinationStrategy<T, R> strategy;
    private R result;

    public Combiner(CombinationStrategy<T, R> strategy)
    {
        this.strategy = Objects.requireNonNull(strategy, "La stratégie ne peut pas être null");
        this.result = strategy.getInitialValue();
    }

    /**
     * Combine tous les éléments de l'itérable donné.
     *
     * @param thingsToCombine L'itérable des éléments à combiner
     * @throws NullPointerException si thingsToCombine est null
     */
    public void combineAll(Iterable<T> thingsToCombine)
    {
        Objects.requireNonNull(thingsToCombine, "L'itérable ne peut pas être null");
        for (T each : thingsToCombine)
        {
            this.combineOne(each);
        }
    }

    /**
     * Combine un seul élément.
     *
     * @param thingToCombine L'élément à combiner
     */
    public void combineOne(T thingToCombine)
    {
        this.result = this.strategy.combineOne(thingToCombine, this.result);
    }

    /**
     * Vérifie si la combinaison doit être faite un par un.
     *
     * @return true si la combinaison doit être faite un par un, false sinon
     */
    public boolean useCombineOne()
    {
        return this.strategy.useCombineOne();
    }

    /**
     * Retourne le résultat de la combinaison.
     *
     * @return Le résultat de la combinaison
     */
    public R getResult()
    {
        return this.result;
    }
}
