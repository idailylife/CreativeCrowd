package edu.inlab.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by inlab-dell on 2017/3/2.
 */
public class GeneralUtils {
    public static List<Integer> reserviorSample(int N, int k){
        List<Integer> results = new ArrayList<>(k);
        Random random = new Random();
        for(int i=0; i<N; i++){
            if(i < k)
                results.add(i);
            else {
                int next = random.nextInt(i+1);
                if(next < k)
                    results.set(next, i);
            }
        }
        Collections.sort(results);
        return results;
    }
}
