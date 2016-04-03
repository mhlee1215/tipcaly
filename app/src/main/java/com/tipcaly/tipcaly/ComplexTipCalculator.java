package com.tipcaly.tipcaly;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tipcaly.tipcaly.utils.CustomFormatter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ComplexTipCalculator extends Fragment {

    Context context;
    ListView lv;
    View rootView;
    List<ComplexUser> userList;
    ComplexListAdapter adapter;
    private List<String> billAmount;
    int selectedRow = -1;
    SeekBar tipSeekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        billAmount = new LinkedList<>();
        for(int i = 0 ; i < SimpleTipCalculator.DIGIT_RANGE ; i++) billAmount.add("0");

        context = getActivity();
        rootView = inflater.inflate(R.layout.fragment_complex, container, false);


        TextView textViewBillAmount = (TextView)rootView.findViewById(R.id.bill_amount);
        textViewBillAmount.setText(CustomFormatter.customFormat("###0.00", 0));
        CustomFormatter.setHighlight(textViewBillAmount, getResources());

        userList = new ArrayList<ComplexUser>();

//        for (int i = 0; i < 1; i++) {
//
//            userList.add(new ComplexUser());
//        }



        lv = (ListView) rootView.findViewById(R.id.users);

//            final View header = inflater.inflate(R.layout.complex_list_header, null, false) ;
//            View footer = inflater.inflate(R.layout.complex_list_footer, null, false) ;
        // listView에 header, footer 추가.
//            lv.addHeaderView(header) ;
//            lv.addFooterView(footer) ;


//            userList.get(0).setIsEditing(true);
//            userList.get(2).setIsEditing(true);

        adapter = new ComplexListAdapter(this, userList);
        lv.setAdapter(adapter);
        lv.setItemsCanFocus(true);





        {Button button = (Button)rootView.findViewById(R.id.button_1);
            button.setOnClickListener(new MyComplexClickListener(this, "1"));}
        {Button button = (Button)rootView.findViewById(R.id.button_2);
            button.setOnClickListener(new MyComplexClickListener(this, "2"));}
        {Button button = (Button)rootView.findViewById(R.id.button_3);
            button.setOnClickListener(new MyComplexClickListener(this, "3"));}
        {Button button = (Button)rootView.findViewById(R.id.button_4);
            button.setOnClickListener(new MyComplexClickListener(this, "4"));}
        {Button button = (Button)rootView.findViewById(R.id.button_5);
            button.setOnClickListener(new MyComplexClickListener(this, "5"));}
        {Button button = (Button)rootView.findViewById(R.id.button_6);
            button.setOnClickListener(new MyComplexClickListener(this, "6"));}
        {Button button = (Button)rootView.findViewById(R.id.button_7);
            button.setOnClickListener(new MyComplexClickListener(this, "7"));}
        {Button button = (Button)rootView.findViewById(R.id.button_8);
            button.setOnClickListener(new MyComplexClickListener(this, "8"));}
        {Button button = (Button)rootView.findViewById(R.id.button_9);
            button.setOnClickListener(new MyComplexClickListener(this, "9"));}
        {Button button = (Button)rootView.findViewById(R.id.button_0);
            button.setOnClickListener(new MyComplexClickListener(this, "0"));}
        {Button button = (Button)rootView.findViewById(R.id.button_del);
            button.setOnClickListener(new MyComplexClickListener(this, "del"));}
        {Button button = (Button)rootView.findViewById(R.id.button_C);
            button.setOnClickListener(new MyComplexClickListener(this, "C"));}

        {Button button = (Button)rootView.findViewById(R.id.button_up);
            button.setOnClickListener(new MyComplexClickListener(this, "up"));}
        {Button button = (Button)rootView.findViewById(R.id.button_down);
            button.setOnClickListener(new MyComplexClickListener(this, "down"));}
//        {Button button = (Button)rootView.findViewById(R.id.button_clear);
//            button.setOnClickListener(new MyComplexClickListener(this, "clear"));}
        {Button button = (Button)rootView.findViewById(R.id.button_add);
            button.setOnClickListener(new MyComplexClickListener(this, "add"));}

        {
            LinearLayout button = (LinearLayout)rootView.findViewById(R.id.bill_layout);
            button.setOnClickListener(new MyComplexClickListener(this, "bill_focus"));}


        int defaultTipRatio = SimpleTipCalculator.defaultTipRatio;
        tipSeekBar = (SeekBar)rootView.findViewById(R.id.seek1);
        tipSeekBar.setProgress(defaultTipRatio);
        tipSeekBar.setOnSeekBarChangeListener(new MyComplexSeekBarChangeListener(this, defaultTipRatio));

        return rootView;
    }

    public void selectUpperRow(){
        if(selectedRow > -1)
            selectRow(selectedRow-1);
    }

    public void selectBelowRow(){
        if(selectedRow < userList.size()-1)
            selectRow(selectedRow+1);
    }

    public void selectRow(int position){
        System.out.println("selectRow: "+position);
        if(selectedRow == -1){
            TextView textViewBillAmount = (TextView)rootView.findViewById(R.id.bill_amount);
            CustomFormatter.setDeHighlight(textViewBillAmount, getResources());
        }
        if(selectedRow >= 0 && selectedRow < userList.size()){
            userList.get(selectedRow).setIsEditing(false);
        }


        selectedRow = position;
        if(position >= 0 && position < userList.size()) {
            userList.get(position).setIsEditing(true);

        }
        if(position == -1){
            TextView textViewBillAmount = (TextView)rootView.findViewById(R.id.bill_amount);
            CustomFormatter.setHighlight(textViewBillAmount, getResources());
        }

        if(selectedRow >= 0) {
            System.out.println("Select!!:" + selectedRow);
            //lv.setSelection(selectedRow);
            //lv.setNextFocusForwardId(selectedRow);
            lv.smoothScrollToPosition(selectedRow);
        }

        adapter.notifyDataSetChanged();

    }


    public class MyComplexSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener
    {
        ComplexTipCalculator act;
        int progressChanged = 0;
        public MyComplexSeekBarChangeListener(ComplexTipCalculator act, int defaultTipRatio){
            this.act=act;
            act.updateTipRatio(defaultTipRatio);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progressChanged = progress;
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

    public class MyComplexClickListener implements View.OnClickListener
    {
        ComplexTipCalculator act;
        String num;
        public MyComplexClickListener(ComplexTipCalculator act, String num) {
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


    public void addNumber(String num){
        System.out.println("selectedRow: "+selectedRow);
        if("del".equalsIgnoreCase(num)){
            if(selectedRow == -1) {
                billAmount.remove(billAmount.size()-1);
                billAmount.add(0, "0");
            }else{
                userList.get(selectedRow).addNumber(num);
            }

        }
        else if("c".equalsIgnoreCase(num)){
            if(selectedRow == -1) {
                billAmount.clear();
                for (int i = 0; i < 7; i++) billAmount.add("0");
            }else{
                userList.get(selectedRow).addNumber(num);
            }
        }
        else if("up".equalsIgnoreCase(num)) {
            selectUpperRow();
        } else if("down".equalsIgnoreCase(num)) {
            selectBelowRow();
        }
        else if("clear".equalsIgnoreCase(num)) {
        }
        else if("bill_focus".equalsIgnoreCase(num)) {
            selectRow(-1);
        }
        else if("add".equalsIgnoreCase(num)) {
            userList.add(new ComplexUser());
            selectRow(userList.size()-1);
        }


        else{
            if(selectedRow == -1) {
                billAmount.add(num);
                billAmount.remove(0);
            }
            else{
                userList.get(selectedRow).addNumber(num);
            }
        }


        updateNumbers();


    }

    public void updateNumbers(){
        TextView textViewBillAmount = (TextView)rootView.findViewById(R.id.bill_amount);
        textViewBillAmount.setText(CustomFormatter.customFormat("###0.00", getFloatAmount()));


        int ratio = tipSeekBar.getProgress();
        float totalAmount = getFloatAmount()*(ratio+100)/100;

        float userMenuSum = 0.0f;
        for(int i = 0 ; i < userList.size() ; i++){
            userMenuSum += userList.get(i).getMenuPrice();
        }
        for(int i = 0 ; i < userList.size() ; i++){
            if(userMenuSum == 0){
                userList.get(i).setSubTotal(0.0f);
                continue;
            }
            float userPrice = totalAmount*(userList.get(i).getMenuPrice()/userMenuSum);
            userList.get(i).setSubTotal(userPrice);
        }


//            TextView textViewTipAmount = (TextView)rootView.findViewById(R.id.tip_amount);
//            textViewTipAmount.setText(CustomFormatter.customFormat("###0.00", tipAmount));
//
//            TextView textViewTotalAmount = (TextView)rootView.findViewById(R.id.total_amount);
//            textViewTotalAmount.setText(CustomFormatter.customFormat("###0.00", tipAmount+getFloatAmount()));

        adapter.notifyDataSetChanged();
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

    public void updateTipRatio(int num){
        TextView textTipRatio = (TextView)rootView.findViewById(R.id.tip_ratio);
        textTipRatio.setText(Integer.toString(num)+"%");
        updateNumbers();
    }
}
