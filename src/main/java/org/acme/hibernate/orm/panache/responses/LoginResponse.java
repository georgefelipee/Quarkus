package org.acme.hibernate.orm.panache.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {

        @JsonProperty("msg")
        private String message;

        @JsonProperty("token")
        private String token;

        // Construtor
        public LoginResponse(String message, String token) {
            this.message = message;
            this.token = token;
        }

        // Getters e Setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

}
