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

import org.eclipse.collections.impl.utility.Iterate;

/**
 * Une interface représentant un combinateur qui peut fusionner les résultats de plusieurs procédures parallèles.
 * Cette interface est utilisée pour combiner les résultats de tâches exécutées en parallèle.
 *
 * @param <T> Le type des éléments à combiner
 */
public interface Combiner<T> extends Serializable
{
    /**
     * Combine tous les éléments d'une collection itérable.
     * Par défaut, cette méthode appelle {@link #combineOne} pour chaque élément.
     *
     * @param thingsToCombine la collection d'éléments à combiner
     * @throws NullPointerException si thingsToCombine est null
     * @throws CombinerException si une erreur survient pendant la combinaison
     */
    default void combineAll(Iterable<T> thingsToCombine)
    {
        Objects.requireNonNull(thingsToCombine, "thingsToCombine ne peut pas être null");
        try
        {
            Iterate.forEach(thingsToCombine, this::combineOne);
        }
        catch (Exception e)
        {
            throw new CombinerException("Erreur lors de la combinaison des éléments", e);
        }
    }

    /**
     * Combine un seul élément.
     *
     * @param thingToCombine l'élément à combiner
     * @throws NullPointerException si thingToCombine est null
     * @throws CombinerException si une erreur survient pendant la combinaison
     */
    void combineOne(T thingToCombine);

    /**
     * Indique si la méthode {@link #combineOne} doit être utilisée pour combiner les résultats.
     * Si false, la méthode {@link #combineAll} sera utilisée à la place.
     *
     * @return true si combineOne doit être utilisé, false sinon
     */
    boolean useCombineOne();

    /**
     * Vérifie si le combinateur est valide pour une utilisation.
     * Par défaut, retourne toujours true. Les implémentations peuvent surcharger cette méthode
     * pour ajouter des validations spécifiques.
     *
     * @return true si le combinateur est valide, false sinon
     * @throws CombinerValidationException si le combinateur n'est pas valide
     */
    default boolean isValid()
    {
        return true;
    }

    /**
     * Réinitialise l'état du combinateur.
     * Par défaut, ne fait rien. Les implémentations peuvent surcharger cette méthode
     * pour réinitialiser leur état interne.
     *
     * @throws CombinerException si une erreur survient pendant la réinitialisation
     */
    default void reset()
    {
        // Ne fait rien par défaut
    }
}

/**
 * Exception levée lorsqu'une erreur survient pendant la combinaison des éléments.
 */
class CombinerException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public CombinerException(String message)
    {
        super(message);
    }

    public CombinerException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

/**
 * Exception levée lorsqu'une validation du combinateur échoue.
 */
class CombinerValidationException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public CombinerValidationException(String message)
    {
        super(message);
    }

    public CombinerValidationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
