package com.zhongmei.yunfu.context.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.zhongmei.yunfu.context.util.ArgsUtils.notNull;


public class CollectionUtils {
    private CollectionUtils() {
    }


    public static <T, E> List<T> sucker(Collection<E> values, Extractable<T, E> extractable) {
        ArgsUtils.notNull(extractable, "Extractable must not be null");
        if (values == null) {
            return null;
        }
        List<T> list = new ArrayList<>();
        for (E value : values) {
            list.add(extractable.extract(value));
        }
        return list;
    }

    public static <E> boolean addAllIgnoreNull(Collection<E> collection, Collection<E> c) {
        if (collection == null) {
            throw new NullPointerException("The collection must not be null");
        }
        return c != null && collection.addAll(c);
    }


    public static <E> boolean addIgnoreNull(final Collection<E> collection, final E object) {
        if (collection == null) {
            throw new NullPointerException("The collection must not be null");
        }
        return object != null && collection.add(object);
    }


    public static <O> Collection<O> select(final Iterable<? extends O> inputCollection,
                                           final Predicate<? super O> predicate) {
        final Collection<O> answer = inputCollection instanceof Collection<?> ?
                new ArrayList<O>(((Collection<?>) inputCollection).size()) : new ArrayList<O>();
        return select(inputCollection, predicate, answer);
    }


    public static <O, R extends Collection<? super O>> R select(final Iterable<? extends O> inputCollection,
                                                                final Predicate<? super O> predicate, final R outputCollection) {

        if (inputCollection != null && predicate != null) {
            for (final O item : inputCollection) {
                if (predicate.evaluate(item)) {
                    outputCollection.add(item);
                }
            }
        }
        return outputCollection;
    }


    public static <T> boolean filter(Collection<T> collection, Predicate<T> predicate) {
        boolean result = false;
        if (collection == null) {
            return result;
        }
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (predicate.evaluate(iterator.next())) {
                iterator.remove();
                result = true;
            }
        }
        return result;
    }

    public static <K, V> Map<K, V> sumOf(Collection<V> values, Extractable<K, V> extractable) {
        ArgsUtils.notNull(extractable, "Extractable must not be null");
        if (values == null) {
            return null;
        }
        Map<K, V> map = new HashMap<>();
        for (V value : values) {
            map.put(extractable.extract(value), value);
        }
        return map;
    }



    public interface Extractable<T, E> {

        T extract(E value);
    }

    public interface Predicate<T> {


        boolean evaluate(T value);

    }

    public static <T> void filterNull(Collection<T> collection) {
        filter(collection, new Predicate<T>() {
            @Override
            public boolean evaluate(T value) {
                return value == null;
            }
        });
    }

}
