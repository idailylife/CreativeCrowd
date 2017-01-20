package edu.inlab.utils;

import java.util.*;

/**
 * Created by hebowei on 2017/1/20.
 */
public class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private final Random random;
    private double total = 0;

    public RandomCollection(Random random){
        this.random = random;
    }

    public RandomCollection(){
        this(new Random());
    }

    public void add(double weight, E result){
        if (weight <= 0)
            return;
        total += weight;
        map.put(total, result);
    }

    public E next(){
        double value = random.nextDouble() * total;
        return map.ceilingEntry(value).getValue();
    }

    public Set<E> nextSet(int size){
        if(size > map.size()){
            System.err.println("RandomCollection: sample size > content size! return null");
            return null;
        }
        Set<E> results = new HashSet<E>();
        while(results.size() < size){
            results.add(next());
        }
        return results;
    }
}
