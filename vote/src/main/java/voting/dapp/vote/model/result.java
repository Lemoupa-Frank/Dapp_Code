package voting.dapp.vote.model;

import java.io.Serializable;

public class result implements Serializable {
    String message,  code;
    public result (String message, String code)
    {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
