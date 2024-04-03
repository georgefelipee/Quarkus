package org.acme.hibernate.orm.panache.dto;

import org.acme.hibernate.orm.panache.User;

public class UserDTO {
    private Long id;
    private String name;
    private Integer age;
    private String username;
    private String email;

    // Construtor
    public UserDTO(Long id ,String name, Integer age, String username, String email) {
        this.name = name;
        this.age = age;
        this.username = username;
        this.email = email;
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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
