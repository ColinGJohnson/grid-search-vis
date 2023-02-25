package net.colinjohnson.grid;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private final Random random;
    private double total = 0;

    public RandomCollection() {
        this(new Random());
    }

    public RandomCollection(Random random) {
        this.random = random;
    }

    public RandomCollection<E> add(double weight, E item) {

        // negative weights won't work because they will never be selected
        if (weight <= 0) {
            return this;
        }

        // add the weight of this entry to the total weight and add
        total += weight;
        map.put(total, item);

        // return this randomCollection so add calls can be chained
        return this;
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
}