package com.newsaggregator;

/**
 * Created by kunalwagle on 05/04/2017.
 */
public class Utils {

    public static int numberOfTimesACharacterOccursInAString(String haystack, Character needle) {
        int count = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }

}
