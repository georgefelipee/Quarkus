package org.acme.hibernate.orm.panache;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
@Table(name="users")
public class User extends  PanacheEntity {

    @Column(length = 255,nullable = false)
    public String name;

    @Column(nullable = false)
    public Integer age;

    @Column(unique = true)
    public String username;
    @Column(unique = true)
    public String email;
    @Column
    public String password;


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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
