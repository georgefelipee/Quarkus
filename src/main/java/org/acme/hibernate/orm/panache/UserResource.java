package org.acme.hibernate.orm.panache;

import io.quarkus.elytron.security.common.BcryptUtil;

import jakarta.annotation.security.RolesAllowed;
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
import org.acme.hibernate.orm.panache.dto.LoginDTO;
import org.acme.hibernate.orm.panache.dto.UserDTO;
import org.acme.hibernate.orm.panache.exceptions.ResponseError;
import org.acme.hibernate.orm.panache.responses.LoginResponse;
import org.acme.hibernate.orm.panache.services.JwtServices;
import org.acme.hibernate.orm.panache.services.UserService;


import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/users")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces({MediaType.APPLICATION_JSON})
public class UserResource {

    @Inject
    UserService userService;
    @Inject
    JwtServices jwtServices;

    @Inject
    Validator validator;

    @POST
    @Transactional
    public Response createUser ( CreateUserRequestDTo userRequestDTo)  {
        Set<ConstraintViolation<CreateUserRequestDTo>> violations = validator.validate(userRequestDTo);

        if(!violations.isEmpty()){
            ResponseError responseError = ResponseError.createFromValidation(violations);
            return Response.status(Response.Status.BAD_REQUEST).entity(responseError).build();
        }

        User user = new User();
        user.setName(userRequestDTo.getName());
        user.setAge(userRequestDTo.getAge());

        user.setEmail(userRequestDTo.getEmail());
        user.setUsername(userRequestDTo.getUsername());

        String password = BcryptUtil.bcryptHash(userRequestDTo.getPassword());
        user.setPassword(password);

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
    @RolesAllowed("admin")
    public Response listAllUsers(){
        List<User> users = User.findAll().list();
        List<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getName(),
                        user.getAge(),
                        user.getUsername(),
                        user.getEmail()
                ))
                .collect(Collectors.toList());
        return Response.ok(userDTOs).status(200).build();

    }

    @DELETE
    @Path("{id}")
    @Transactional
    @RolesAllowed("admin")
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
    @RolesAllowed("admin")
    public Response updateUser( Long id, CreateUserRequestDTo userRequestDTo){
        User userEntity = User.findById(id);
        if(userEntity == null){
            return  Response.status(Response.Status.NOT_FOUND).build();
        }

        userEntity.setName(userRequestDTo.getName());
        userEntity.setAge(userRequestDTo.getAge());

        return  Response.ok().build();
    }

    @POST
    @Path("/login")
    @Transactional
    public Response login(@Valid LoginDTO loginRequestDTO){
        if(loginRequestDTO.getEmail() == null && loginRequestDTO.getUsername() == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Email ou nome de usuário é obrigatório!").build();
        }

        if(loginRequestDTO.getEmail() == null){
           User user = userService.findByUsername(loginRequestDTO.getUsername());
           if(user == null){
                return Response.status(Response.Status.NOT_FOUND).entity("Usuário não encontrado!").build();
           }

           if(!user.getPassword().equals(loginRequestDTO.getPassword())){
                return Response.status(Response.Status.BAD_REQUEST).entity("Senha inválida!").build();
           }

           return Response.ok().status(200).entity("Login feito com sucesso!").build();

        } else {
            User user = userService.findByEmail(loginRequestDTO.getEmail());
            if(user == null){
                return Response.status(Response.Status.NOT_FOUND).entity("Usuário não encontrado!").build();
            }
            boolean passwordCompare = BcryptUtil.matches(loginRequestDTO.getPassword(), user.getPassword());

            if(!passwordCompare){
                return Response.status(Response.Status.BAD_REQUEST).entity("Senha inválida!").build();
            }

            String token =  jwtServices.generateToken(user.getUsername());
            LoginResponse response = new LoginResponse("Login feito com sucesso!", token);

            return Response.ok().status(200).entity(response).build();
        }

    }



}
