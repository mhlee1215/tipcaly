package com.tipcaly.tipcaly.utils;

import java.text.DecimalFormat;

/**
 * Created by mhlee on 4/1/16.
 */
public class CustomFormatter {
    static public String customFormat(String pattern, double value ) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(value);
        return output;
    }
}
