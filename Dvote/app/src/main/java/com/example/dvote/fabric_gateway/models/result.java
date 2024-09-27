package com.example.dvote.fabric_gateway.models;

import java.io.Serializable;

public class result implements Serializable {
    String Message,  code;
    public result (String Message, String code)
    {
        this.code = code;
        this.Message = Message;
    }

    public String getMessage() {
        return Message;
    }

    public String getCode() {
        return code;
    }




}
