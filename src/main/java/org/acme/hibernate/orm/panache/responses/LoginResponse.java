package org.acme.hibernate.orm.panache.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.acme.hibernate.orm.panache.User;
import org.acme.hibernate.orm.panache.dto.UserDTO;

public class LoginResponse {

        @JsonProperty("msg")
        private String message;

        @JsonProperty("token")
        private String token;
        @JsonProperty("user")
        private UserDTO user;

        // Construtor
        public LoginResponse(String message, String token, UserDTO user) {
            this.message = message;
            this.token = token;
            this.user = user;
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
