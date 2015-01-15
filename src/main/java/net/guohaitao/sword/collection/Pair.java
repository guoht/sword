package net.guohaitao.sword.collection;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Created by i@guohaitao.net on 14-12-31.
 * Description: Pair对象
 */
public class Pair<F, S> implements Serializable, Comparable<Pair<F, S>> {
    private static final long serialVersionUID = 3991215893770958546L;
    private F first;
    private S second;

    @Nonnull
    public F getFirst() {
        return first;
    }


    @Nonnull
    public S getSecond() {
        return second;
    }


    /**
     * private constructor. Should not be called outside of the class. Use
     * Pair.of() instead.
     *
     * @param first
     *         first object of pair
     * @param second
     *         second object of pair
     */
    private Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * construct a Pair object
     *
     * @param first
     *         first object of pair
     * @param second
     *         second object of pair
     * @return a Pair object
     */
    public static <F, S> Pair<F, S> of(@Nonnull F first, @Nonnull S second) {
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(second);
        return new Pair<>(first, second);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Pair.class)
                .add("first", first)
                .add("second", second)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(first, second);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pair other = (Pair) obj;
        if (first == null) {
            if (other.first != null)
                return false;
        } else if (!first.equals(other.first))
            return false;
        if (second == null) {
            if (other.second != null)
                return false;
        } else if (!second.equals(other.second))
            return false;
        return true;
    }

    @Override
    public int compareTo(@Nonnull Pair<F, S> o) {
        int c = compare(first, o.first);
        return c == 0 ? compare(second, o.second) : c;
    }


    /**
     * compare function to implement <code>compareTo</code>
     * change between <code>compare(Object s, Object f)</code> and <code>compare(Object f, Object s)</code>
     * to switch between Ascending sort and Descending sort
     *
     * @param s
     *         second object
     * @param f
     *         first object
     * @return -1 if f<s
     */
    @SuppressWarnings("unchecked")
    private int compare(Object s, Object f) {
        return f == null ? s == null ? 0 : -1 : s == null ? +1
                : ((Comparable<Object>) f).compareTo(s);
    }
}