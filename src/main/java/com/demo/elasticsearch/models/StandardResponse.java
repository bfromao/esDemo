package com.demo.elasticsearch.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StandardResponse {
    private boolean result;
    private String message;

    /***
     * Creates a new instance of {@link StandardResponse}.
     */
    public StandardResponse(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    /***
     * Creates a new instance of {@link StandardResponse}.
     */
    public StandardResponse() {
    }

    public void setSuccess(){
        this.result= true;
        this.message = null;
    }

    public void setFail(String message){
        this.result= false;
        this.message = message;
    }
}
