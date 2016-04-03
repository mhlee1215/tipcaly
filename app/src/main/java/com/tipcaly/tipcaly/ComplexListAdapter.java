package com.tipcaly.tipcaly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.tipcaly.tipcaly.utils.CustomFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mhlee on 4/1/16.
 */
public class ComplexListAdapter extends BaseSwipeAdapter {

    ComplexTipCalculator act = null;
    List<ComplexUser> userList;
    private static LayoutInflater inflater=null;

    public ComplexListAdapter(ComplexTipCalculator act, List<ComplexUser> userList){
        this.act = act;
        this.userList = userList;
//        this.userList = new ArrayList<ComplexUser>();
//        this.userList.addAll(userList);

        inflater = ( LayoutInflater )act.getActivity().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    static View rowView2;
    @Override
    public View generateView(int position, ViewGroup parent) {
        View rowView;

        rowView = inflater.inflate(R.layout.complex_list_item, null);
        rowView2 = rowView;

        SwipeLayout swipeLayout = (SwipeLayout)rowView.findViewById(getSwipeLayoutResourceId(position));


        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                 YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(act.getActivity(), "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });

        rowView.findViewById(R.id.delete).setOnClickListener(new MyDeleteClickListener(act, swipeLayout, position));
        rowView.findViewById(R.id.reset).setOnClickListener(new MyResetClickListener(act, swipeLayout, position));



        TextView viewMenuPrice =(TextView) rowView.findViewById(R.id.menu_price);
        TextView viewSubTotal =(TextView) rowView.findViewById(R.id.sub_total);
        float menuPrice = this.userList.get(position).getMenuPrice();
        viewMenuPrice.setText(CustomFormatter.customFormat("###0.00$", menuPrice));
        float subTotal = this.userList.get(position).getSubTotal();
        viewSubTotal.setText(CustomFormatter.customFormat("###0.00$", subTotal));

        rowView.setOnClickListener(new MyRowClickListener(act, position));

        ImageView buttonTrashOpen =(ImageView) rowView.findViewById(R.id.trash_open);
        buttonTrashOpen.setOnClickListener(new MyDeleteButtonClickListener(act, swipeLayout, true));

        ImageView buttonTrashClose =(ImageView) rowView.findViewById(R.id.trash);
        buttonTrashClose.setOnClickListener(new MyDeleteButtonClickListener(act, swipeLayout, false));

        return rowView;
    }


    @Override
    public void fillValues(int position, View convertView) {
        TextView viewMenuPrice =(TextView) convertView.findViewById(R.id.menu_price);
        TextView viewSubTotal =(TextView) convertView.findViewById(R.id.sub_total);

        if(userList.get(position).isEditing())
        {
            CustomFormatter.setHighlight(viewMenuPrice, act.getActivity().getResources());
        }else{
            CustomFormatter.setDeHighlight(viewMenuPrice, act.getActivity().getResources());
        }

        float menuPrice = this.userList.get(position).getMenuPrice();
        viewMenuPrice.setText(CustomFormatter.customFormat("###0.00$", menuPrice));
        float subTotal = this.userList.get(position).getSubTotal();
        viewSubTotal.setText(CustomFormatter.customFormat("###0.00$", subTotal));


    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<ComplexUser> getData(){
        return this.userList;
    }

    public class MyDeleteButtonClickListener implements View.OnClickListener {
        ComplexTipCalculator act;
        SwipeLayout swipeLayout;
        boolean isOpen;

        public MyDeleteButtonClickListener(ComplexTipCalculator act, SwipeLayout swipeLayout, boolean isOpen){
            this.act = act;
            this.isOpen = isOpen;
            this.swipeLayout = swipeLayout;
        }

        @Override
        public void onClick(View v) {
            if(isOpen)
                swipeLayout.open(true);
            else
                swipeLayout.close(true);
        }
    }

    public class MyDeleteClickListener implements View.OnClickListener {
        ComplexTipCalculator act;
        SwipeLayout swipeLayout;
        int position;

        public MyDeleteClickListener(ComplexTipCalculator act, SwipeLayout swipeLayout, int position){
            this.act = act;
            this.position = position;
            this.swipeLayout = swipeLayout;
        }

        @Override
        public void onClick(View v) {
            userList.remove(position);
            swipeLayout.close(false);
            act.selectUpperRow();
            act.updateNumbers();
        }
    }

    public class MyResetClickListener implements View.OnClickListener {
        ComplexTipCalculator act;
        SwipeLayout swipeLayout;
        int position;

        public MyResetClickListener(ComplexTipCalculator act, SwipeLayout swipeLayout, int position){
            this.act = act;
            this.position = position;
            this.swipeLayout = swipeLayout;
        }

        @Override
        public void onClick(View v) {
            userList.get(position).addNumber("c");
            swipeLayout.close(false);
            act.updateNumbers();

        }
    }

    public class MyRowClickListener implements View.OnClickListener {
        ComplexTipCalculator act;
        int position;

        public MyRowClickListener(ComplexTipCalculator act, int position){
            this.act = act;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            act.selectRow(position);
        }
    }
}