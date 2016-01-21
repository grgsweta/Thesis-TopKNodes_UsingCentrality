/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testproject;

import java.util.Comparator;
import java.util.HashMap;

/**
 *
 * @author useradmin
 */
public class ValueComparator implements Comparator<Integer> {
    
    HashMap<Integer, Double> base;
    public ValueComparator(HashMap<Integer, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(Integer a, Integer b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
