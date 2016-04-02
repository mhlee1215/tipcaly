package com.tipcaly.tipcaly;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tipcaly.tipcaly.utils.CustomFormatter;

import java.util.ArrayList;
import java.util.List;

public class ComplexTipCalculator extends Fragment {


        ListView lv;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_complex, container, false);


            TextView textViewBillAmount = (TextView)rootView.findViewById(R.id.bill_amount);
            textViewBillAmount.setText(CustomFormatter.customFormat("###0.00", 0));


            List<ComplexUser> userList = new ArrayList<ComplexUser>();

            for (int i = 0; i < 5; i++) {

                userList.add(new ComplexUser());
            }



            lv = (ListView) rootView.findViewById(R.id.users);

            final View header = inflater.inflate(R.layout.complex_list_header, null, false) ;
            View footer = inflater.inflate(R.layout.complex_list_footer, null, false) ;

            // listView에 header, footer 추가.
//            lv.addHeaderView(header) ;
//            lv.addFooterView(footer) ;



            lv.setAdapter(new ComplexListAdapter(getActivity(), userList));


//            String  textString = "StackOverFlow Rocks!!!";
//            Spannable spanText = Spannable.Factory.getInstance().newSpannable(textString);
//            spanText.setSpan(new BackgroundColorSpan(0xFFFFFF00), 14, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            return rootView;
        }
    }