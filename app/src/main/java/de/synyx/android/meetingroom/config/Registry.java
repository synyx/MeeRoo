package de.synyx.android.meetingroom.config;

import android.support.annotation.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Registry for components to decouple the creation, storage, and usage of them. Always use the interface (if present)
 * as type when calling {@link #put(Class, Object)}, {@link #get(Class)} or {@link #remove(Class)}.
 *
 * <p>For Testing purpose, there exists a {@link #putOverride(Class, Object)} method. If a component is put in both
 * methods, the component from the override will be used. After each test, if used, a call to {@link #clearOverrides()}
 * is needed to not affect other tests.</p>
 *
 * @author  Tobias Knell - knell@synyx.de
 */
public class Registry {

    private static Map<Class<?>, Object> components = new ConcurrentHashMap<>();
    private static Map<Class<?>, Object> override = new ConcurrentHashMap<>();

    /**
     * Get the component with the given type from the registry.
     *
     * @param  type  the type of the component to get, should be the interface, if there is one
     * @param  <T>  the interface/superclass of the component to get
     *
     * @return  the component
     *
     * @throws  IllegalStateException  if the component was not found
     */
    @NonNull
    public static <T> T get(Class<T> type) {

        Object o = override.get(type);

        if (o != null) {
            return type.cast(o);
        }

        o = components.get(type);

        if (o == null) {
            throw new IllegalStateException(String.format(
                    "No instance of type %s found. Did you forget to register it?", type.getName()));
        }

        return type.cast(o);
    }


    /**
     * Add the given component to the registry.
     *
     * @param  type  the type of the component to put, should be the interface, if there is one
     * @param  instance  the instance of the component
     * @param  <T>  interface/superclass of the component
     */
    public static <T> void put(@NonNull Class<T> type, @NonNull T instance) {

        components.put(type, type.cast(instance));
    }


    /**
     * Remove the given type of component from the registry.
     *
     * @param  type  the type of the component to remove, should be the interface, if there is one
     */
    public static void remove(@NonNull Class type) {

        components.remove(type);
    }


    /**
     * For testing purpose, overrides components of the same type that are registered in {@link #put(Class, Object)},
     * but does not replace them.
     *
     * @param  type  the type of the component to put, should be the interface, if there is one
     * @param  instance  the instance of the component
     * @param  <T>  interface/superclass of the component
     */
    public static <T> void putOverride(@NonNull Class<T> type, @NonNull T instance) {

        override.put(type, type.cast(instance));
    }


    /**
     * For testing purpose, clears all overrides, so the real components will be used again.
     */
    public static void clearOverrides() {

        override.clear();
    }
}
