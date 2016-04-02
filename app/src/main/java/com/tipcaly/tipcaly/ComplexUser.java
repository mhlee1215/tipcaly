package com.tipcaly.tipcaly;

/**
 * Created by mhlee on 4/2/16.
 */
public class ComplexUser {
    boolean isEditing;
    float menuPrice;
    float subTotal;

    public ComplexUser(){
        this.isEditing = false;
        this.menuPrice = 0.0f;
        this.subTotal = 0.0f;
    }

    public boolean isEditing() {
        return isEditing;
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
}
