package com.distributedSystems.ReplicationJarProject.Jar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

public class Product  implements Serializable {

    public Product(String type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    String type;

    @Override
    public String toString() {
        return "{ type: "+type+" }";
    }

    public String toJson(){
        //Creating the ObjectMapper object
        ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString
        String jsonString ="";

        {
            try {
                jsonString = mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return jsonString;
    }

}
