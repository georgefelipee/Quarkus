package org.acme.hibernate.orm.panache.resources;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.hibernate.orm.panache.User;
import org.acme.hibernate.orm.panache.dto.CreateAccountDTO;
import org.acme.hibernate.orm.panache.dto.DepositAccountDTO;
import org.acme.hibernate.orm.panache.dto.TransferAccountDTO;
import org.acme.hibernate.orm.panache.enums.AccountType;
import org.acme.hibernate.orm.panache.exceptions.ErrorResponseEdit;
import org.acme.hibernate.orm.panache.exceptions.ResponseError;

import jakarta.validation.Validator;
import org.acme.hibernate.orm.panache.models.Account;
import org.acme.hibernate.orm.panache.models.Agency;
import org.acme.hibernate.orm.panache.models.TypeAccount;

import java.math.BigDecimal;
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

        typeAccount.persist();
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

    @POST
    @Path("/deposit/{id}")
    @Transactional
    public Response depositAccount(@PathParam("id") Long id, DepositAccountDTO depositRequestDTO){
        //Recebo em REAL R$1,00
        Account account = Account.findById(id);
        if(account == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Conta não encontrada!").build();
        }
        // Convertendo o valor do depósito para centavos
        BigDecimal depositAmount = depositRequestDTO.getBalance().multiply(BigDecimal.valueOf(100));
        System.out.println(" testee " + depositAmount);

        // Adicionando o valor do depósito ao saldo da conta
        BigDecimal balanceInCents = account.getBalance();
        BigDecimal newBalanceInCents = balanceInCents.add(depositAmount);

        // Atualizando o saldo da conta
        account.setBalance(newBalanceInCents);

        return Response.status(200).entity("Depósito cocluído!").build();

    }

    @POST
    @Path("/transfer")
    @Transactional
    public Response transferAccount(@Valid TransferAccountDTO transferRequestDTO){
        Account accountOrigin = Account.findById(transferRequestDTO.getUser_origin_id());
        if (accountOrigin == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Conta de origem não encontrada!").build();
        }
        Account accountDestiny = Account.findById(transferRequestDTO.getUser_destiny_id());
        if (accountDestiny == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Conta de destino não encontrada!").build();
        }

        BigDecimal transferValue = transferRequestDTO.getTrasnferValue().multiply(BigDecimal.valueOf(100));
        BigDecimal balanceOrigin = accountOrigin.getBalance();

        if (balanceOrigin.compareTo(transferValue) < 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Saldo insuficiente!").build();
        }

        BigDecimal newBalanceOrigin = balanceOrigin.subtract(transferValue);
        accountOrigin.setBalance(newBalanceOrigin);

        BigDecimal balanceDestiny = accountDestiny.getBalance();
        BigDecimal newBalanceDestiny = balanceDestiny.add(transferValue);
        accountDestiny.setBalance(newBalanceDestiny);

        return Response.status(200).entity("Transferência cocluída!").build();
    }



}
