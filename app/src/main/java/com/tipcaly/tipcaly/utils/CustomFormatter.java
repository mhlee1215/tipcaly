package com.tipcaly.tipcaly.utils;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.widget.TextView;

import com.tipcaly.tipcaly.R;

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


    static public void setDeHighlight(TextView v, Resources r){
        v.setTypeface(null, Typeface.NORMAL);
        v.setTextColor(r.getColor(R.color.white));
    }

    static public void setHighlight(TextView v, Resources r){
        v.setTypeface(null, Typeface.BOLD);
        v.setTextColor(r.getColor(R.color.orange_peel));
    }
}
