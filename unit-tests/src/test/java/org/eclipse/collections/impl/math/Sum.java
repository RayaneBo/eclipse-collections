/*
 * Copyright (c) 2021 Goldman Sachs.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.math;

import java.io.Serializable;

/**
 * A sum is a mutable or immutable object that may have either other objects or values added to it.
 *
 * @deprecated Don't use in new tests
 */
@Deprecated
public interface Sum extends Serializable
{
    /**
     * Récupère la valeur actuelle de la somme.
     *
     * @return la valeur actuelle de la somme
     */
    Number getValue();

    /**
     * Crée une nouvelle instance de la même implémentation avec une valeur initiale de zéro.
     *
     * @return une nouvelle instance de la somme
     */
    Sum speciesNew();

    /**
     * Ajoute une valeur entière à la somme.
     *
     * @param value la valeur entière à ajouter
     * @return cette somme après l'ajout
     */
    Sum add(int value);

    /**
     * Ajoute un nombre à la somme.
     *
     * @param number le nombre à ajouter
     * @return cette somme après l'ajout
     */
    Sum add(Number number);

    /**
     * Ajoute une autre somme à cette somme.
     *
     * @param otherSum l'autre somme à ajouter
     * @return cette somme après l'ajout
     */
    Sum add(Sum otherSum);

    /**
     * Ajoute un objet à la somme.
     * L'objet doit être convertible en nombre.
     *
     * @param object l'objet à ajouter
     * @return cette somme après l'ajout
     * @throws ClassCastException si l'objet ne peut pas être converti en nombre
     */
    Sum add(Object object);
}
