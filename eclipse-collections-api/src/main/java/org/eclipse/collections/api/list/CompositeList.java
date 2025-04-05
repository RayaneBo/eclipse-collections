package org.eclipse.collections.api.list;

import java.util.List;

/**
 * Une interface représentant une liste composite qui est composée d'une ou plusieurs sous-listes.
 * Cette interface définit le comportement de base d'une liste composite.
 *
 * @param <E> Le type des éléments de la liste
 */
public interface CompositeList<E> extends MutableList<E>
{
    /**
     * Ajoute une sous-liste à la liste composite.
     *
     * @param list La sous-liste à ajouter
     * @return true si la liste a été modifiée
     */
    boolean addComposited(List<E> list);

    /**
     * Retourne le nombre de sous-listes dans la liste composite.
     *
     * @return Le nombre de sous-listes
     */
    int getCompositeCount();

    /**
     * Retourne la sous-liste à l'index spécifié.
     *
     * @param index L'index de la sous-liste
     * @return La sous-liste à l'index spécifié
     * @throws IndexOutOfBoundsException si l'index est invalide
     */
    List<E> getComposite(int index);

    /**
     * Fusionne toutes les sous-listes en une seule liste.
     * Cette opération peut être coûteuse en termes de performance.
     */
    void flatten();
} 