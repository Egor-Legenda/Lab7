package org.example.common.response;

import java.io.Serializable;

public abstract class Response implements Serializable {
    private String name;
    private String error;
    private String result;

    public Response(String name, String error, String result) {
        this.name = name;
        this.error = error;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public String getError() {
        return error;
    }

    public String getResult() {
        return result;
    }
}
