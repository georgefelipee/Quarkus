package org.acme.hibernate.orm.panache.resources;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.hibernate.orm.panache.User;
import org.acme.hibernate.orm.panache.dto.CreateAccountDTO;
import org.acme.hibernate.orm.panache.enums.AccountType;
import org.acme.hibernate.orm.panache.exceptions.ErrorResponseEdit;
import org.acme.hibernate.orm.panache.exceptions.ResponseError;

import jakarta.validation.Validator;
import org.acme.hibernate.orm.panache.models.Account;
import org.acme.hibernate.orm.panache.models.Agency;
import org.acme.hibernate.orm.panache.models.TypeAccount;

import java.util.Set;

@Path("/accounts")
@ApplicationScoped
@Consumes("application/json")
@Produces("application/json")
public class AccountResource {

    @Inject
    Validator validator;

    @POST
    @Transactional
    public Response createAccount(CreateAccountDTO accountRequestDTO){

        Set<ConstraintViolation<CreateAccountDTO>> violations = validator.validate(accountRequestDTO);

        if(!violations.isEmpty()){
            ResponseError responseError = ResponseError.createFromValidation(violations);
            return Response.status(Response.Status.BAD_REQUEST).entity(responseError).build();
        }

        Account account = new Account();
        TypeAccount typeAccount = new TypeAccount();

        User user = User.findById(accountRequestDTO.getUser_id());
        if(user == null){
            ErrorResponseEdit errorResponse = new ErrorResponseEdit( "Usuario não encontrado!");
            return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
        }
        account.setUser_id(user);

        Agency agency = Agency.findById(accountRequestDTO.getAgency_id());
        if(agency == null){
            ErrorResponseEdit errorResponse = new ErrorResponseEdit( "Agência não encontrada!");
            return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
        }
        account.setAgency_id(agency);

        if (accountRequestDTO.getRole() == 1) {
            typeAccount.setAccountType(AccountType.SPECIAL);
            typeAccount.setHasCreditCard(accountRequestDTO.isHasCreditCard());

        } else if(accountRequestDTO.getRole() == 2) {
            typeAccount.setAccountType(AccountType.PREMIUM);
            typeAccount.setHasCreditCard(accountRequestDTO.isHasCreditCard());
            typeAccount.setHasLis(accountRequestDTO.isHasLis());
        }

        account.setTypeAccount_id(typeAccount);
        account.persist();

        return Response.ok().status(201).build();
    }

    @GET
    public Response getAccounts(){
        PanacheQuery<PanacheEntityBase> query = Account.findAll();
        return Response.ok(query.list()).status(200).build();
    }

    @GET
    @Path("/{id}")
    @Transactional
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
    public Response deleteAccount(@PathParam("id") Long id){
        Account account = Account.findById(id);
        if(account == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Conta não encontrada!").build();
        }
        account.delete();
        return Response.status(Response.Status.NO_CONTENT).build();
    }



}
