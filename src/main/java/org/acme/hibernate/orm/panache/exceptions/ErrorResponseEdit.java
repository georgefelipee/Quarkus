package org.acme.hibernate.orm.panache.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponseEdit {
    @JsonProperty("msg")
    private String message;

    public ErrorResponseEdit(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
