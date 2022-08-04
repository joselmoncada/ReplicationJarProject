package com.distributedSystems.ReplicationJarProject.Producer;

import java.io.Serializable;
import java.util.Date;

public class BackUpRequest implements Serializable {

   private int product_A_amount;
   private int getProduct_B_amount;

    public int getProduct_A_amount() {
        return product_A_amount;
    }

    public void setProduct_A_amount(int product_A_amount) {
        this.product_A_amount = product_A_amount;
    }

    public int getGetProduct_B_amount() {
        return getProduct_B_amount;
    }

    public void setGetProduct_B_amount(int getProduct_B_amount) {
        this.getProduct_B_amount = getProduct_B_amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;

    public BackUpRequest(int product_A_amount, int getProduct_B_amount, Date date) {
        this.product_A_amount = product_A_amount;
        this.getProduct_B_amount = getProduct_B_amount;
        this.date = date;
    }
}
