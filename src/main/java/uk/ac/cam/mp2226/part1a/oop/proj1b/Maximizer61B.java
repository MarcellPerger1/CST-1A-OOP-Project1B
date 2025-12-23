package uk.ac.cam.mp2226.part1a.oop.proj1b;

import java.util.Comparator;
//import java.util.stream.StreamSupport;

public class Maximizer61B {
    /**
     * Returns the maximum element from the given iterable of comparables.
     * You may assume that the iterable contains no nulls.
     *
     * @param iterable  the Iterable of T
     * @return          the maximum element
     */
    public static <T extends Comparable<T>> T max(Iterable<T> iterable) {
        T val = null;
        for(T item : iterable) {
            if (val == null || item.compareTo(val) < 0) val = item;
        }
        return val;
        // Or if we can use java.util:
//        return StreamSupport.stream(iterable.spliterator(), true)
//                .min(Comparator.naturalOrder())
//                .orElse(null);
    }

    /**
     * Returns the maximum element from the given iterable according to the specified comparator.
     * You may assume that the iterable contains no nulls.
     *
     * @param iterable  the Iterable of T
     * @param comp      the Comparator to compare elements
     * @return          the maximum element according to the comparator
     */
    public static <T> T max(Iterable<T> iterable, Comparator<T> comp) {
        T val = null;
        for(T item : iterable) {
            if (val == null || comp.compare(item, val) > 0) val = item;
        }
        return val;
        // Or if we can use java.util:
//        return StreamSupport.stream(iterable.spliterator(), true)
//                .max(comp)
//                .orElse(null);
    }

    public static void main(String[] args) {
        // The style checker will complain about this main method, feel free to delete.

//         ArrayDeque61B<Integer> ad = new ArrayDeque61B<>();
//         ad.addLast(5);
//         ad.addLast(12);
//         ad.addLast(17);
//         ad.addLast(23);
//         System.out.println(max(ad));
    }
}
