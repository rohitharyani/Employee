package com.ecarus.employee;



/**
 * Created by Rohit Haryani on 26-Jan-17.
 */

public class PreviousOrderDetailsList {

    private String mProductName, mProductTotalQuantity, mProductTotalBill, mProductTotalWeight;

    public PreviousOrderDetailsList(String mProductName, String mProductTotalQuantity, String mProductBill, String mProductTotalWeight) {
        this.mProductName = mProductName;
        this.mProductTotalQuantity = mProductTotalQuantity;
        this.mProductTotalBill = mProductBill;
        this.mProductTotalWeight = mProductTotalWeight;

    }

    public String getmProductName() {
        return mProductName;
    }

    public String getmProductTotalQuantity() { return mProductTotalQuantity; }

    public String getmProductTotalBill() {
        return mProductTotalBill;
    }

    public String getmProductTotalWeight() {
        return mProductTotalWeight;
    }

}
