package org.acme.hibernate.orm.panache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.PasswordType;
import jakarta.persistence.*;
import org.acme.hibernate.orm.panache.models.Account;

import java.util.List;

@Entity
@Table(name="users")
public class User extends  PanacheEntity {

    @Column(length = 255,nullable = false)
    public String name;

    @Column(unique = true)
    public String username;
    @Column(unique = true)
    public String email;
    @Column
    public String password;

    @OneToMany(mappedBy = "user_id", cascade = CascadeType.REMOVE)
    @JsonIgnore
    public List<Account> accounts;

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


    public Long getId() {
        return id;
    }
}
