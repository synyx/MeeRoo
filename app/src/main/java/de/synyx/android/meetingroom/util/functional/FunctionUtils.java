package de.synyx.android.meetingroom.util.functional;

import android.arch.core.util.Function;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class FunctionUtils {

    private FunctionUtils() {

        // hide
    }

    public static <T, R> List<R> mapList(Collection<T> list, Function<T, R> mapper) {

        List<R> result = new ArrayList<>(list.size());

        for (T item : list) {
            result.add(mapper.apply(item));
        }

        return result;
    }


    public static <T, K, V> Map<K, V> toMap(Collection<T> list, Function<T, K> keyMapper, Function<T, V> valueMapper) {

        Map<K, V> result = new HashMap<>(list.size());

        for (T item : list) {
            result.put(keyMapper.apply(item), valueMapper.apply(item));
        }

        return result;
    }


    public static <T, R> R[] toArray(Collection<T> collection, Class<R> clazz) {

        return collection.toArray((R[]) Array.newInstance(clazz, collection.size()));
    }


    public static <T, R> R[] mapToArray(Collection<T> collection, Function<T, R> mapper, Class<R> clazz) {

        return toArray(mapList(collection, mapper), clazz);
    }
}
