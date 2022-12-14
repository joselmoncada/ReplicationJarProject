package com.distributedSystems.ReplicationJarProject.Jar;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;



@Data
//@AllArgsConstructor
@NoArgsConstructor
public class Register implements Serializable {

    String name;
    String type;
    int amount;
    String operation;
    int remaining;
    Date date;

    public Register(String name, String type, int amount, String operation, int remaining) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.operation = operation;
        this.remaining = remaining;
        this.date = new Date();
    }

    public Register(String name, String type, int amount, String operation) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.operation = operation;
        this.remaining = -1;
        this.date = new Date();
    }

    public Register(String name, String type, int amount, String operation, int remaining, Date date) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.operation = operation;
        this.remaining = remaining;
        this.date = date;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "{ name: "+name+", \n " +
                "type: "+type+", \n "+
                "amount: "+amount+", \n "+
                "operation: "+operation+", \n "+
                "remaining: "+remaining+", \n "+
                "date: "+date+", \n "+
                " }";
    }
}
