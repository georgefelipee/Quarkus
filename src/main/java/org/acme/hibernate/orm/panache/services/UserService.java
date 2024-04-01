package org.acme.hibernate.orm.panache.services;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.hibernate.orm.panache.User;

@ApplicationScoped
public class UserService {

    public boolean isEmailExists(String email){
        PanacheQuery<PanacheEntityBase> query = User.find("email", email);
        return query.count() > 0;
    }

    public boolean isUsernameExists(String username){
        PanacheQuery<PanacheEntityBase> query = User.find("username", username);
        return query.count() > 0;
    }

    public User findByUsername(String username){
        return User.find("username", username).firstResult();
    }

    public User findByEmail(String email){
        return User.find("email", email).firstResult();
    }
    public User findByEmailOrUsername(String emailOrUsername){
        return User.find("email = ?1 or username = ?2", emailOrUsername, emailOrUsername).firstResult();
    }


}
