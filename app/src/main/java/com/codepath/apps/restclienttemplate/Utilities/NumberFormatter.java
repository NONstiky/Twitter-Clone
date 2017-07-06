package com.codepath.apps.restclienttemplate.Utilities;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by mbanchik on 7/6/17.
 */


 public  class NumberFormatter {

    private static char[] c = new char[]{'k', 'm', 'b', 't'};

    public static String format(long n,int iteration) {
        if(n < 1000){
            return String.valueOf(n);
        }
        double d = ((long) n / 100) / 10.0;
        boolean isRound = (d * 10) %10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
        return (d < 1000? //this determines the class, i.e. 'k', 'm' etc
                ((d > 99.9 || isRound || (!isRound && d > 9.99)? //this decides whether to trim the decimals
                        (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
                ) + "" + c[iteration]) : format((long) d, iteration+1));
    }

}
