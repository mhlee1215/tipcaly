package com.tipcaly.tipcaly;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tipcaly.tipcaly.utils.CustomFormatter;

import java.util.LinkedList;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
public class SimpleTipCalculator extends Fragment {
    public static final String PREV_TIP_RATIO = "PREV_TIP_RATIO";
    public static final int DIGIT_RANGE = 7;
    public static final int defaultTipRatio = 15;
    public static List<String> billAmount;
    View rootView;
    SeekBar tipSeekBar;

    SharedPreferences prefs = null;
    SharedPreferences.Editor editor = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = prefs.edit();

        billAmount = new LinkedList<>();
        for(int i = 0 ; i < DIGIT_RANGE ; i++) billAmount.add("0");

        rootView = inflater.inflate(R.layout.fragment_simple, container, false);


        TextView textViewBillAmount = (TextView)rootView.findViewById(R.id.bill_amount);
        textViewBillAmount.setText(CustomFormatter.customFormat("###0.00", 0));

        CustomFormatter.setHighlight(textViewBillAmount, getResources());

        TextView textViewTipAmount = (TextView)rootView.findViewById(R.id.tip_amount);
        textViewTipAmount.setText(CustomFormatter.customFormat("###0.00", 0));

        TextView textViewTotalAmount = (TextView)rootView.findViewById(R.id.total_amount);
        textViewTotalAmount.setText(CustomFormatter.customFormat("###0.00", 0));


        {Button button = (Button)rootView.findViewById(R.id.button_1);
        button.setOnClickListener(new MyClickListener(this, "1"));}
        {Button button = (Button)rootView.findViewById(R.id.button_2);
            button.setOnClickListener(new MyClickListener(this, "2"));}
        {Button button = (Button)rootView.findViewById(R.id.button_3);
            button.setOnClickListener(new MyClickListener(this, "3"));}
        {Button button = (Button)rootView.findViewById(R.id.button_4);
            button.setOnClickListener(new MyClickListener(this, "4"));}
        {Button button = (Button)rootView.findViewById(R.id.button_5);
            button.setOnClickListener(new MyClickListener(this, "5"));}
        {Button button = (Button)rootView.findViewById(R.id.button_6);
            button.setOnClickListener(new MyClickListener(this, "6"));}
        {Button button = (Button)rootView.findViewById(R.id.button_7);
            button.setOnClickListener(new MyClickListener(this, "7"));}
        {Button button = (Button)rootView.findViewById(R.id.button_8);
            button.setOnClickListener(new MyClickListener(this, "8"));}
        {Button button = (Button)rootView.findViewById(R.id.button_9);
            button.setOnClickListener(new MyClickListener(this, "9"));}
        {Button button = (Button)rootView.findViewById(R.id.button_0);
            button.setOnClickListener(new MyClickListener(this, "0"));}
        {Button button = (Button)rootView.findViewById(R.id.button_del);
            button.setOnClickListener(new MyClickListener(this, "del"));}
        {Button button = (Button)rootView.findViewById(R.id.button_C);
            button.setOnClickListener(new MyClickListener(this, "C"));}



        tipSeekBar = (SeekBar)rootView.findViewById(R.id.seek1);

        int tipRatio = prefs.getInt(PREV_TIP_RATIO, defaultTipRatio);
        tipSeekBar.setProgress(tipRatio);
        tipSeekBar.setOnSeekBarChangeListener(new MySeekBarChangeListener(this, tipRatio));

        return rootView;
    }

    public void setBillAmount(List<String> bill){
        billAmount = new LinkedList<>();
        billAmount.addAll(bill);
        updateNumbers();
    }

    public List getBill(){
        return billAmount;
    }

    public void updateExternalTipRatio(){
        int ratio = prefs.getInt(PREV_TIP_RATIO, defaultTipRatio);
        tipSeekBar.setProgress(ratio);
        updateTipRatio(ratio);
    }

    public class MySeekBarChangeListener implements SeekBar.OnSeekBarChangeListener
    {
        SimpleTipCalculator act;
        int progressChanged = 0;
        public MySeekBarChangeListener(SimpleTipCalculator act, int defaultTipRatio){
            this.act=act;
            act.updateTipRatio(defaultTipRatio);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progressChanged = progress;

            editor.putInt(PREV_TIP_RATIO, progress);
            editor.commit();

            act.updateTipRatio(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(getActivity(), "seek bar progress:" + progressChanged,
//                        Toast.LENGTH_SHORT).show();
        }
    }

    public class MyClickListener implements View.OnClickListener
    {
        SimpleTipCalculator act;
        String num;
        public MyClickListener(SimpleTipCalculator act, String num) {
            this.act=act;
            this.num = num;

        }
        @Override
        public void onClick(View v)
        {
            ((MainActivity)act.getActivity()).startGame();
            act.addNumber(num);
        }
    }

    public void updateTipRatio(int num){
        TextView textTipRatio = (TextView)rootView.findViewById(R.id.tip_ratio);
        textTipRatio.setText(Integer.toString(num)+"%");
        updateNumbers();
    }

    public void addNumber(String num){
        if("del".equalsIgnoreCase(num)){

            billAmount.remove(billAmount.size()-1);
            billAmount.add(0, "0");
        }
        else if("c".equalsIgnoreCase(num)){
            billAmount.clear();
            for(int i = 0 ; i < SimpleTipCalculator.DIGIT_RANGE ; i++) billAmount.add("0");
        }
        else{
            billAmount.add(num);
            billAmount.remove(0);
        }


        updateNumbers();


    }

    public void updateNumbers(){
        TextView textViewBillAmount = (TextView)rootView.findViewById(R.id.bill_amount);
        textViewBillAmount.setText(CustomFormatter.customFormat("###0.00", getFloatAmount()));


        int ratio = tipSeekBar.getProgress();
        float tipAmount = getFloatAmount()*ratio/100;

        TextView textViewTipAmount = (TextView)rootView.findViewById(R.id.tip_amount);
        textViewTipAmount.setText(CustomFormatter.customFormat("###0.00", tipAmount));

        TextView textViewTotalAmount = (TextView)rootView.findViewById(R.id.total_amount);
        textViewTotalAmount.setText(CustomFormatter.customFormat("###0.00", tipAmount+getFloatAmount()));

    }

    public String getStringAmount(){
        String strAmount = "";
        if(billAmount.size() == 0)
            strAmount = "0.00";
        else{
            for(int i = 0 ; i < billAmount.size() ; i++){
                //if(!"0".equals(billAmount.get(i)) || i >= billAmount.size() - 3 )
                    strAmount += billAmount.get(i);
                //if(i == billAmount.size() - 3) strAmount +=".";
            }
        }

        return strAmount;
    }

    public float getFloatAmount(){
        String strAmount = getStringAmount();
        return Float.parseFloat(strAmount)/100;
    }
}