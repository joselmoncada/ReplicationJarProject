package com.distributedSystems.ReplicationJarProject.Jar;

import java.io.Serializable;
import java.util.Date;

public class Register implements Serializable{
    String name;
    char type;
    int amount;
    String operation;
    int remaining;
    Date date;

    public Register(String name, char type, int amount, String operation, int remaining) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.operation = operation;
        this.remaining = remaining;
        this.date = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
