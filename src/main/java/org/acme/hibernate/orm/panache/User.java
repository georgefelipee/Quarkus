package org.acme.hibernate.orm.panache;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User extends  PanacheEntity{

    @Column(length = 255,nullable = false)
    public String name;
    @Column(nullable = false)
    public Integer age;

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
