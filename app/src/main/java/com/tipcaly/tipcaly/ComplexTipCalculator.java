package com.tipcaly.tipcaly;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tipcaly.tipcaly.utils.CustomFormatter;

public class ComplexTipCalculator extends Fragment {


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_complex, container, false);


            TextView textViewBillAmount = (TextView)rootView.findViewById(R.id.bill_amount);
            textViewBillAmount.setText(CustomFormatter.customFormat("###0.00", 0));



//            String  textString = "StackOverFlow Rocks!!!";
//            Spannable spanText = Spannable.Factory.getInstance().newSpannable(textString);
//            spanText.setSpan(new BackgroundColorSpan(0xFFFFFF00), 14, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            return rootView;
        }
    }