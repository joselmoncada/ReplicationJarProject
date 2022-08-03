package com.distributedSystems.ReplicationJarProject.Jar;

import java.io.Serializable;
import java.util.Date;

public class Register implements Serializable {

    String name;
    Date date;

    public Register(String name) {
        this.name = name;
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
