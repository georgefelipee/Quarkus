package org.acme.hibernate.orm.panache;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.hibernate.orm.panache.dto.CreateUserRequestDTo;
import org.acme.hibernate.orm.panache.services.UserService;

import java.util.Set;

@Path("/users")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces({MediaType.APPLICATION_JSON})
public class UserResource {

    @Inject
    UserService userService;
    @Inject
    Validator validator;

    @POST
    @Transactional
    public Response createUser ( CreateUserRequestDTo userRequestDTo)  {
        Set<ConstraintViolation<CreateUserRequestDTo>> violations = validator.validate(userRequestDTo);

        if(!violations.isEmpty()){
            ConstraintViolation<CreateUserRequestDTo> erro = violations.stream().findAny().get();
            String ErrorMessage = erro.getMessage();

            return Response.status(Response.Status.BAD_REQUEST).entity(ErrorMessage).build();
        }
        User user = new User();
        user.setName(userRequestDTo.getName());
        user.setAge(userRequestDTo.getAge());

        user.setEmail(userRequestDTo.getEmail());
        user.setUsername(userRequestDTo.getUsername());
        user.setPassword(userRequestDTo.getPassword());

       if(userService.isEmailExists(user.getEmail())) {
           return Response.status(Response.Status.BAD_REQUEST).entity("Email já existe!").build();
       }
       if(userService.isUsernameExists(user.getUsername())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Seu nome de usuário já existe!").build();
       }

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
