package org.acme.hibernate.orm.panache;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.hibernate.orm.panache.dto.CreateUserRequestDTo;

@Path("/users")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces({MediaType.APPLICATION_JSON})
public class UserResource {

    @POST
    @Transactional
    public Response createUser(CreateUserRequestDTo userRequestDTo){
        User user = new User();
        user.setName(userRequestDTo.getName());
        user.setAge(userRequestDTo.getAge());

        user.persist();

        return Response.ok(userRequestDTo).status(201).build();
    }

    @GET
    public Response listAllUsers(){
        PanacheQuery<PanacheEntityBase> query = User.findAll();
        return Response.ok(query.list()).status(200).build();

    }

}
