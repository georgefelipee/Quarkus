package org.acme.hibernate.orm.panache;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;


import jakarta.validation.Valid;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.hibernate.orm.panache.dto.CreateUserRequestDTo;
import org.acme.hibernate.orm.panache.dto.LoginDTO;
import org.acme.hibernate.orm.panache.dto.UpdateUserDTO;
import org.acme.hibernate.orm.panache.dto.UserDTO;

import org.acme.hibernate.orm.panache.responses.LoginResponse;

import org.acme.hibernate.orm.panache.services.JwtServices;
import org.acme.hibernate.orm.panache.services.UserService;
import org.jose4j.jwt.consumer.InvalidJwtException;


import java.util.List;

@Path("/users")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces({MediaType.APPLICATION_JSON})
public class UserResource {

    @Inject
    UserService userService;

    @Inject
    JwtServices jwtServices;

    @POST
    @Transactional
    public Response createUser ( @Valid CreateUserRequestDTo userRequestDTo)  {
        User createUser = userService.createUser(userRequestDTo);

        return Response.ok(createUser).status(201).build();
    }

    @GET
    @RolesAllowed({"admin","user"})
    public Response listAllUsers(){
        List<UserDTO> userList = userService.listAllUsers();

        return Response.ok(userList).status(200).build();

    }

    @DELETE
    @Path("{id}")
    @Transactional
    @RolesAllowed({"admin","user"})
    public Response deleteUser(Long id){
        userService.deleteuser(id);

        return  Response.ok().status(204).build();
    }
    @PUT
    @Path("{id}")
    @Transactional
    @RolesAllowed({"admin","user"})
    public Response updateUser(@Valid Long id, UpdateUserDTO userRequestDTo){
        UserDTO user = userService.updateUser(id, userRequestDTo);

        return  Response.status(Response.Status.OK).entity(user).build();
    }

    @POST
    @Path("/login")
    @Transactional
    public Response login(@Valid LoginDTO loginRequestDTO) {
        User user = null;
        if (loginRequestDTO.getEmail() != null) {
            user = userService.findByEmailOrUsername(loginRequestDTO.getEmail());

        } else if (loginRequestDTO.getUsername() != null) {
            user = userService.findByEmailOrUsername(loginRequestDTO.getUsername());
        }
        UserDTO userDTO = new UserDTO(user.getId(), user.getName(), user.getUsername(), user.getEmail());

        String token = userService.login(loginRequestDTO);
        LoginResponse response = new LoginResponse("Login feito com sucesso!", token, userDTO);

        return Response.ok().status(200).entity(response).build();
    }

    @GET
    @Path("/decode")
    @RolesAllowed({"admin", "user"})
    public Response decodeToken(@HeaderParam("Authorization") String token) throws InvalidJwtException {
        User user = jwtServices.decodeToken(token);
        return Response.ok().status(200).entity(user).build();
    }



}
