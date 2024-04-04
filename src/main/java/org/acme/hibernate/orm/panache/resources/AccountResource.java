package org.acme.hibernate.orm.panache.resources;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.hibernate.orm.panache.User;
import org.acme.hibernate.orm.panache.dto.AccountDTO;
import org.acme.hibernate.orm.panache.dto.CreateAccountDTO;
import org.acme.hibernate.orm.panache.dto.DepositAccountDTO;
import org.acme.hibernate.orm.panache.dto.TransferAccountDTO;
import jakarta.validation.Validator;
import org.acme.hibernate.orm.panache.models.Account;
import org.acme.hibernate.orm.panache.services.AccountServices;
import org.acme.hibernate.orm.panache.services.JwtServices;
import org.jboss.logging.Logger;
import org.jose4j.jwt.consumer.InvalidJwtException;

import java.util.List;
import java.util.stream.Collectors;

@Path("/accounts")
@ApplicationScoped
@Consumes("application/json")
@Produces("application/json")
public class AccountResource {

    @Inject
    Validator validator;

    @Inject
    AccountServices accountServices;

    @Inject
    JwtServices jwtServices;

    @POST
    @Transactional
    public Response createAccount(CreateAccountDTO accountRequestDTO){
        AccountDTO accountDTO = accountServices.createAccount(accountRequestDTO);

        return Response.ok(accountDTO).status(201).build();
    }

    @GET
    public Response getAccounts(){
//        PanacheQuery<PanacheEntityBase> query = Account.findAll();
//        return Response.ok(query.list()).status(200).build();

        List<Account> accounts = Account.findAll().list();
        List<AccountDTO> accountDTOs = accounts.stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());

        return Response.ok(accountDTOs).status(200).build();
    }

    @GET
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"admin", "user"})
    public Response getAccount(Long id){
        Account account = Account.findById(id);
        if(account == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Conta não encontrada!").build();
        }
        return Response.ok(account).status(200).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"admin", "user"})
    public Response deleteAccount(@PathParam("id") Long id){
        Account account = Account.findById(id);
        if(account == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Conta não encontrada!").build();
        }
        account.delete();
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Path("/deposit/{id}")
    @Transactional
    @RolesAllowed({"admin", "user"})
    public Response depositAccount(@PathParam("id") Long id, DepositAccountDTO depositRequestDTO){
        accountServices.depositAccount(id, depositRequestDTO);

        return Response.status(200).entity("Depósito cocluído!").build();
    }

    @POST
    @Path("/transfer")
    @Transactional
    @RolesAllowed({"admin", "user"})
    public Response transferAccount(@Valid TransferAccountDTO transferRequestDTO){

        accountServices.transferAccount(transferRequestDTO);

        return Response.status(200).entity("Transferência concluída!").build();
    }

    @GET
    @Path("/decode")
    @RolesAllowed({"admin", "user"})
    public Response decodeToken(@HeaderParam("Authorization") String token) throws InvalidJwtException {
        User user = jwtServices.decodeToken(token);
        return Response.ok().status(200).entity(user).build();
    }


}
