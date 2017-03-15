package com.sleepwalker.utils;

import java.util.HashSet;
import java.util.Set;

public class SetUtil {

    public static void main(String[] args) {
        Set<Integer> a = new HashSet<Integer>();
        Set<Integer> b = new HashSet<Integer>();
        b.add(1);
        b.add(2);
        b.add(3);

        System.out.println("union:" + union(a, b).toString());
        System.out.println("intersection: " + intersection(a, b).toString());
        System.out.println("difference: " + difference(a, b).toString());
        System.out.println("complement: " + complement(a, b).toString());
    }

    public static <T> Set<T> union(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<T>(a);
        result.addAll(b);
        return result;
    }

    public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<T>(a);
        result.retainAll(b);
        return result;
    }

    public static <T> Set<T> difference(Set<T> superset, Set<T> subset) {
        Set<T> result = new HashSet<T>(superset);
        result.removeAll(subset);
        return result;
    }

    public static <T> Set<T> complement(Set<T> a, Set<T> b) {
        return difference(union(a, b), intersection(a, b));
    }
}
