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


}
