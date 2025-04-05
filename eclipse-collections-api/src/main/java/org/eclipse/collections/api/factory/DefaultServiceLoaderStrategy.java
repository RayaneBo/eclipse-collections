
package org.eclipse.collections.api.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * Implémentation par défaut de la stratégie de chargement des services.
 * Cette implémentation utilise le ServiceLoader standard de Java.
 *
 * @param <T> Le type du service à charger
 */
public class DefaultServiceLoaderStrategy<T> implements ServiceLoaderStrategy<T>
{
    private static final String ERROR_NO_IMPLEMENTATION = "Could not find any implementations of %s. Check that eclipse-collections.jar is on the classpath and that its META-INF/services directory is intact.";
    private static final String ERROR_MULTIPLE_IMPLEMENTATIONS = "Found multiple implementations of %s on the classpath. Check that there is only one copy of eclipse-collections.jar on the classpath. Found implementations: %s.";

    private final Map<String, String> factoryImplementations;

    public DefaultServiceLoaderStrategy()
    {
        this.factoryImplementations = new HashMap<>();
        initializeFactoryImplementations();
    }

    private void initializeFactoryImplementations()
    {
        // Initialisation des implémentations de factory
        factoryImplementations.put("org.eclipse.collections.api.factory.bag.ImmutableBagFactory", "org.eclipse.collections.impl.bag.immutable.ImmutableBagFactoryImpl");
        // ... (copier le reste des implémentations de ServiceLoaderUtils)
    }

    @Override
    public T loadServiceClass(Class<T> serviceClass)
    {
        validateServiceClass(serviceClass);
        
        T serviceInstance = tryLoadWithClassLoaders(serviceClass);
        if (serviceInstance == null)
        {
            serviceInstance = createProxyInstance(serviceClass, 
                String.format(ERROR_NO_IMPLEMENTATION, serviceClass.getSimpleName()));
        }
        return serviceInstance;
    }

    private void validateServiceClass(Class<T> serviceClass)
    {
        if (serviceClass == null)
        {
            throw new IllegalArgumentException("Service class cannot be null");
        }
    }

    private T tryLoadWithClassLoaders(Class<T> serviceClass)
    {
        T serviceInstance = loadServiceClass(serviceClass, Thread.currentThread().getContextClassLoader());
        if (serviceInstance == null)
        {
            serviceInstance = loadServiceClass(serviceClass, DefaultServiceLoaderStrategy.class.getClassLoader());
        }
        if (serviceInstance == null)
        {
            serviceInstance = loadByReflection(serviceClass, Thread.currentThread().getContextClassLoader());
        }
        if (serviceInstance == null)
        {
            serviceInstance = loadByReflection(serviceClass, DefaultServiceLoaderStrategy.class.getClassLoader());
        }
        return serviceInstance;
    }

    private T loadServiceClass(Class<T> serviceClass, ClassLoader classLoader)
    {
        List<T> implementations = new ArrayList<>();
        for (T implementation : ServiceLoader.load(serviceClass, classLoader))
        {
            implementations.add(implementation);
        }
        return handleImplementations(serviceClass, implementations);
    }

    @Override
    public T handleImplementations(Class<T> serviceClass, List<T> implementations)
    {
        if (implementations.isEmpty())
        {
            return null;
        }
        if (implementations.size() > 1)
        {
            String implementationNames = implementations.stream()
                .map(Object::getClass)
                .map(Class::getName)
                .collect(Collectors.joining(", "));
            throw new IllegalStateException(
                String.format(ERROR_MULTIPLE_IMPLEMENTATIONS, serviceClass.getSimpleName(), implementationNames));
        }
        return implementations.get(0);
    }

    @Override
    public T loadByReflection(Class<T> serviceClass, ClassLoader classLoader)
    {
        String implementationClassName = this.factoryImplementations.get(serviceClass.getName());
        if (implementationClassName != null)
        {
            try
            {
                Class<?> implementationClass = Class.forName(implementationClassName, true, classLoader);
                return serviceClass.cast(implementationClass.getConstructor().newInstance());
            }
            catch (Exception e)
            {
                return null;
            }
        }
        return null;
    }

    @Override
    public T createProxyInstance(Class<T> serviceClass, String errorMessage)
    {
        InvocationHandler handler = (proxy, method, args) -> {
            throw new IllegalStateException(errorMessage);
        };
        return serviceClass.cast(Proxy.newProxyInstance(
            serviceClass.getClassLoader(),
            new Class<?>[] { serviceClass },
            handler));
    }
} 