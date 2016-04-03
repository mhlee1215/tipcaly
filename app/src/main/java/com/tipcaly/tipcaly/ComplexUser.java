package com.tipcaly.tipcaly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mhlee on 4/2/16.
 */
public class ComplexUser {
    private boolean isEmpty;
    private boolean isEditing;
    private float menuPrice;
    private float subTotal;
    private List<String> billAmount;

    public ComplexUser(){
        this.isEmpty = false;
        this.isEditing = false;
        this.menuPrice = 0.0f;
        this.subTotal = 0.0f;
        billAmount = new ArrayList<String>();
        for(int i = 0 ; i < SimpleTipCalculator.DIGIT_RANGE ; i++) billAmount.add("0");
    }

    public ComplexUser(boolean isEmpty){
        this.isEmpty = isEmpty;
    }



    public boolean isEditing() {
        return isEditing;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void setIsEditing(boolean isEditing) {
        this.isEditing = isEditing;
    }

    public float getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(float menuPrice) {
        this.menuPrice = menuPrice;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public List<String> getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(List<String> billAmount) {
        this.billAmount = billAmount;
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


        menuPrice = getFloatAmount();
        System.out.println("menuPrice: "+menuPrice);

    }

    @Override
    public String toString() {
        return "ComplexUser{" +
                "isEmpty=" + isEmpty +
                ", isEditing=" + isEditing +
                ", menuPrice=" + menuPrice +
                ", subTotal=" + subTotal +
                '}';
    }
}
