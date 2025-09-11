package com.saicone.gama.util.jar;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.GenericDeclaration;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarFile;

/**
 * An object that save classes from a code source.<br>
 * This class offers utility methods to iterate hover cached classes.
 *
 * @author Rubenicos
 */
public class JarRuntime extends LinkedHashMap<String, Class<?>> {

    /**
     * Create a JarRuntime with a class to read classes from its code source.
     *
     * @param clazz the class to read classes from its code source.
     * @return      a newly generated JarRuntime.
     * @throws IOException if an I/O error occurs.
     */
    @NotNull
    public static JarRuntime valueOf(@NotNull Class<?> clazz) throws IOException {
        return valueOf(clazz.getClassLoader(), clazz);
    }

    /**
     * Create a JarRuntime with specified class loader and class to read classes from its code source.
     *
     * @param classLoader the class loader to get classes.
     * @param clazz       the class to read classes from its code source.
     * @return            a newly generated JarRuntime.
     * @throws IOException if an I/O error occurs.
     */
    @NotNull
    public static JarRuntime valueOf(@NotNull ClassLoader classLoader, @NotNull Class<?> clazz) throws IOException {
        return valueOf(classLoader, clazz.getProtectionDomain().getCodeSource().getLocation());
    }

    /**
     * Create a JarRuntime with the default class loader and url to read classes from it.
     *
     * @param url the url to read classes from it.
     * @return    a newly generated JarRuntime.
     * @throws IOException if an I/O error occurs.
     */
    @NotNull
    public static JarRuntime valueOf(@NotNull URL url) throws IOException {
        return valueOf(JarRuntime.class.getClassLoader(), url);
    }

    /**
     * Create a JarRuntime with specified class loader and url to read classes from it.
     *
     * @param classLoader the class loader to get classes.
     * @param url         the url to read classes from it.
     * @return            a newly generated JarRuntime.
     * @throws IOException if an I/O error occurs.
     */
    @NotNull
    public static JarRuntime valueOf(@NotNull ClassLoader classLoader, @NotNull URL url) throws IOException {
        File file;
        try {
            try {
                file = new File(url.toURI());
            } catch (IllegalArgumentException e) {
                file = new File(((JarURLConnection) url.openConnection()).getJarFileURL().toURI());
            }
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        }
        try (JarFile jarFile = new JarFile(file)) {
            return valueOf(classLoader, jarFile);
        }
    }

    /**
     * Create a JarRuntime with the default class loader and jar file to read classes from it.
     *
     * @param file the jar file to read classes from it.
     * @return     a newly generated JarRuntime.
     */
    @NotNull
    public static JarRuntime valueOf(@NotNull JarFile file) {
        return valueOf(JarRuntime.class.getClassLoader(), file);
    }

    /**
     * Create a JarRuntime with specified class loader and jar file to read classes from it.
     *
     * @param classLoader the class loader to get classes.
     * @param file        the jar file to read classes from it.
     * @return            a newly generated JarRuntime.
     */
    @NotNull
    public static JarRuntime valueOf(@NotNull ClassLoader classLoader, @NotNull JarFile file) {
        final JarRuntime jarRuntime = new JarRuntime(classLoader);
        file.stream().filter(entry -> entry.getName().endsWith(".class")).forEach(entry -> {
            final String name = entry.getName();
            final String parsedName = name.replace('/', '.').substring(0, name.length() - 6);
            jarRuntime.put(parsedName);
        });
        return jarRuntime;
    }

    private final ClassLoader classLoader;

    /**
     * Create a new JarRuntime with the default class loader.
     */
    public JarRuntime() {
        this(JarRuntime.class.getClassLoader());
    }

    /**
     * Create a new JarRuntime with the specified parameters.
     *
     * @param classLoader the class loader to get classes.
     */
    public JarRuntime(@NotNull ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Get the class loader used to load classes.
     *
     * @return a class loader.
     */
    @NotNull
    public ClassLoader getLoader() {
        return classLoader;
    }

    /**
     * Get an iterable of classes that are subclasses of the specified type.
     *
     * @param type the superclass or interface to filter classes.
     * @param <T>  the type of the superclass or interface.
     * @return     an iterable of classes that are subclasses of the specified type.
     */
    @NotNull
    public <T> Iterable<Class<? extends T>> subClasses(@NotNull Class<T> type) {
        return iterableOf(type::isAssignableFrom, clazz -> clazz.asSubclass(type));
    }

    /**
     * Get an iterable of classes that are annotated with the specified annotation type.
     *
     * @param annotationType the annotation type to filter classes.
     * @return               an iterable of classes that are annotated with the specified annotation type.
     */
    @NotNull
    public Iterable<Class<?>> annotated(@NotNull Class<? extends Annotation> annotationType) {
        return iterableOf(clazz -> {
            if (clazz.isAnnotation() || clazz.getSuperclass() == null) {
                return false;
            }
            return clazz.isAnnotationPresent(annotationType)
                    || isAnnotationPresent(clazz.getDeclaredConstructors(), annotationType)
                    || isAnnotationPresent(clazz.getDeclaredFields(), annotationType)
                    || isAnnotationPresent(clazz.getDeclaredMethods(), annotationType)
                    || isAnnotationPresent(clazz.getTypeParameters(), annotationType);
        });
    }

    private boolean isAnnotationPresent(@NotNull AnnotatedElement[] elements, @NotNull Class<? extends Annotation> annotationType) {
        for (@NotNull AnnotatedElement element : elements) {
            if (element.isAnnotationPresent(annotationType)) {
                return true;
            }
            if (element instanceof GenericDeclaration && isAnnotationPresent(((GenericDeclaration) element).getTypeParameters(), annotationType)) {
                return true;
            }
            if (element instanceof Executable && isAnnotationPresent(((Executable) element).getParameters(), annotationType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get an iterable of classes that match the specified predicate.
     *
     * @param predicate a predicate to filter classes.
     * @return          an iterable of classes that match the specified predicate.
     */
    @NotNull
    public Iterable<Class<?>> iterableOf(@NotNull Predicate<Class<?>> predicate) {
        return iterableOf(predicate, clazz -> clazz);
    }

    /**
     * Get an iterable of mapped classes that match the specified predicate.
     *
     * @param predicate a predicate to filter classes.
     * @param function  a function to map classes.
     * @param <T>       the type of the mapped classes.
     * @return          an iterable of mapped classes that match the specified predicate.
     */
    @NotNull
    public <T> Iterable<T> iterableOf(@NotNull Predicate<Class<?>> predicate, @NotNull Function<Class<?>, T> function) {
        return new Iterable<T>() {
            @Override
            public @NotNull Iterator<T> iterator() {
                return new Iterator<T>() {
                    private final Iterator<Map.Entry<String, Class<?>>> iterator = JarRuntime.this.entrySet().iterator();
                    private T found;

                    @Override
                    public boolean hasNext() {
                        if (found != null) {
                            return true;
                        }
                        while (iterator.hasNext()) {
                            final Class<?> next = iterator.next().getValue();
                            if (predicate.test(next)) {
                                found = function.apply(next);
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override
                    public T next() {
                        if (found == null) {
                            throw new NoSuchElementException();
                        }
                        final T result = found;
                        found = null;
                        return result;
                    }
                };
            }
        };
    }

    /**
     * Reload all classes that failed to load previously.
     *
     * @return this JarRuntime instance.
     */
    @NotNull
    @Contract("-> this")
    public JarRuntime reload() {
        for (String name : new HashSet<>(this.keySet())) {
            if (this.get(name) == Object.class) {
                put(name);
            }
        }
        return this;
    }

    /**
     * Put a class in the runtime cache.
     * If the class cannot be loaded, it will be cached as {@code Object.class}.
     *
     * @param name the class name to load and put.
     * @return     the previously cached class, or null if there was no mapping for the class name.
     */
    @Nullable
    public Class<?> put(@NotNull String name) {
        try {
            // Avoid initialization, it can fail with NotClassDefFoundError
            final Class<?> clazz = Class.forName(name, false, this.classLoader);
            return this.put(name, clazz);
        } catch (Throwable t) {
            return this.put(name, Object.class);
        }
    }
}
