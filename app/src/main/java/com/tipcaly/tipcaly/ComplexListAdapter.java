package com.tipcaly.tipcaly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tipcaly.tipcaly.utils.CustomFormatter;

import java.util.List;

/**
 * Created by mhlee on 4/1/16.
 */
public class ComplexListAdapter extends BaseAdapter {

    Context context = null;
    List<ComplexUser> userList;
    private static LayoutInflater inflater=null;

    public ComplexListAdapter(Context context, List<ComplexUser> userList){
        this.context = context;
        this.userList = userList;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView;
        rowView = inflater.inflate(R.layout.complex_list_item, null);
        TextView viewMenuPrice =(TextView) rowView.findViewById(R.id.menu_price);
        TextView viewSubTotal =(TextView) rowView.findViewById(R.id.sub_total);
        float menuPrice = this.userList.get(position).getMenuPrice();
        viewMenuPrice.setText(CustomFormatter.customFormat("###0.00$", menuPrice));
        float subTotal = this.userList.get(position).getSubTotal();
        viewSubTotal.setText(CustomFormatter.customFormat("###0.00$", subTotal));


//        rowView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                TextView viewMain =(TextView) v.findViewById(R.id.textTitle);
//                Toast.makeText(context, "You Clicked " + viewMain.getText(), Toast.LENGTH_LONG).show();
//            }
//        });
        return rowView;
    }
}