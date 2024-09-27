package com.example.dvote.fabric_gateway.retrofit;

public class result {
    String message,  code;
    public result (String message, String code)
    {
        this.code = code;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
