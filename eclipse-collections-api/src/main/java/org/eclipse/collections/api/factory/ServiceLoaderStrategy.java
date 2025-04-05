package org.eclipse.collections.api.factory;

import java.util.List;

/**
 * Interface définissant la stratégie de chargement des services.
 * Permet d'implémenter différentes stratégies de chargement selon les besoins.
 *
 * @param <T> Le type du service à charger
 */
public interface ServiceLoaderStrategy<T>
{
    /**
     * Charge une implémentation de service.
     *
     * @param serviceClass La classe du service à charger
     * @return Une instance du service
     * @throws IllegalArgumentException si la classe de service est null
     */
    T loadServiceClass(Class<T> serviceClass);

    /**
     * Gère les implémentations trouvées pour un service.
     *
     * @param serviceClass La classe du service
     * @param implementations Les implémentations trouvées
     * @return Une instance du service
     */
    T handleImplementations(Class<T> serviceClass, List<T> implementations);

    /**
     * Charge une implémentation de service en utilisant la réflexion.
     *
     * @param serviceClass La classe du service
     * @param classLoader Le ClassLoader à utiliser
     * @return Une instance du service ou null si aucune implémentation n'est trouvée
     */
    T loadByReflection(Class<T> serviceClass, ClassLoader classLoader);

    /**
     * Crée une instance proxy pour un service.
     *
     * @param serviceClass La classe du service
     * @param errorMessage Le message d'erreur à afficher
     * @return Une instance proxy du service
     */
    T createProxyInstance(Class<T> serviceClass, String errorMessage);
} 