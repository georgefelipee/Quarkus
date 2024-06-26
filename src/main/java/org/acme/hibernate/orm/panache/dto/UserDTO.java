package org.acme.hibernate.orm.panache.dto;

import org.acme.hibernate.orm.panache.User;

public class UserDTO {
    private Long id;
    private String name;

    private String username;
    private String email;

    // Construtor
    public UserDTO(Long id ,String name, String username, String email) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
