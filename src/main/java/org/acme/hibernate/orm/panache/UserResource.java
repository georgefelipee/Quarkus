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

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(Long id){
        User userEntity = User.findById(id);
        if(userEntity == null){
          return  Response.status(Response.Status.NOT_FOUND).build();
        }
        userEntity.delete();
        return  Response.ok().status(204).build();
    }
    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser( Long id, CreateUserRequestDTo userRequestDTo){
        User userEntity = User.findById(id);
        if(userEntity == null){
            return  Response.status(Response.Status.NOT_FOUND).build();
        }

        userEntity.setName(userRequestDTo.getName());
        userEntity.setAge(userRequestDTo.getAge());

        return  Response.ok().build();
    }



}
