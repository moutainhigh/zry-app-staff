package com.zhongmei.yunfu.context.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.zhongmei.yunfu.context.util.ArgsUtils.notNull;

/**
 * Created by demo on 2018/12/15
 */
public class CollectionUtils {
    private CollectionUtils() {
    }

    /**
     * Return a list that extract from collection
     *
     * @param values      The collection will be extracted
     * @param extractable An interface {@link Extractable}
     * @param <T>         Type of list
     * @param <E>         Type of values
     * @return
     */
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

    /**
     * Adds an element to the collection unless the element is null.
     *
     * @param <E>        the type of object the {@link Collection} contains
     * @param collection the collection to add to, must not be null
     * @param object     the object to add, if null it will not be added
     * @return true if the collection changed
     * @throws NullPointerException if the collection is null
     * @since copy from apache
     */
    public static <E> boolean addIgnoreNull(final Collection<E> collection, final E object) {
        if (collection == null) {
            throw new NullPointerException("The collection must not be null");
        }
        return object != null && collection.add(object);
    }

    /**
     * Selects all elements from input collection which match the given
     * predicate into an output collection.
     * <p>
     * A <code>null</code> predicate matches no elements.
     *
     * @param <O>             the type of object the {@link Iterable} contains
     * @param inputCollection the collection to get the input from, may not be null
     * @param predicate       the predicate to use, may be null
     * @return the elements matching the predicate (new list)
     * @throws NullPointerException if the input collection is null
     */
    public static <O> Collection<O> select(final Iterable<? extends O> inputCollection,
                                           final Predicate<? super O> predicate) {
        final Collection<O> answer = inputCollection instanceof Collection<?> ?
                new ArrayList<O>(((Collection<?>) inputCollection).size()) : new ArrayList<O>();
        return select(inputCollection, predicate, answer);
    }

    /**
     * Selects all elements from input collection which match the given
     * predicate and adds them to outputCollection.
     * <p>
     * If the input collection or predicate is null, there is no change to the
     * output collection.
     *
     * @param <O>              the type of object the {@link Iterable} contains
     * @param <R>              the type of the output {@link Collection}
     * @param inputCollection  the collection to get the input from, may be null
     * @param predicate        the predicate to use, may be null
     * @param outputCollection the collection to output into, may not be null if the inputCollection
     *                         and predicate or not null
     * @return the outputCollection
     */
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

    /**
     * 过滤掉满足条件的
     *
     * @param collection
     * @param predicate  true 过滤掉
     * @param <T>
     * @return
     */
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

    /**
     * Created by demo on 2018/12/15
     * 通用转换接口 E 转换成 T
     */

    public interface Extractable<T, E> {

        T extract(E value);
    }

    public interface Predicate<T> {

        /**
         * Use the specified parameter to perform a test that returns true or false.
         *
         * @param value the object to evaluate, should not be changed
         * @return true or false
         * @throws ClassCastException       (runtime) if the input is the wrong class
         * @throws IllegalArgumentException (runtime) if the input is invalid
         */
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
