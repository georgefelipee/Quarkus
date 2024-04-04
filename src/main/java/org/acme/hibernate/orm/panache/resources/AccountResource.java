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
import org.acme.hibernate.orm.panache.enums.AccountType;
import org.acme.hibernate.orm.panache.exceptions.ErrorResponseEdit;
import org.acme.hibernate.orm.panache.exceptions.ResponseError;

import jakarta.validation.Validator;
import org.acme.hibernate.orm.panache.models.Account;
import org.acme.hibernate.orm.panache.models.Agency;
import org.acme.hibernate.orm.panache.models.TypeAccount;
import org.acme.hibernate.orm.panache.services.AccountServices;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/accounts")
@ApplicationScoped
@Consumes("application/json")
@Produces("application/json")
public class AccountResource {

    public static final Logger log = Logger.getLogger(AccountResource.class);

    @Inject
    Validator validator;

    @Inject
    AccountServices accountServices;

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
        //Recebo em REAL R$1,00
        Account account = Account.findById(id);
        if(account == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Conta não encontrada!").build();
        }
        // Convertendo o valor do depósito para centavos
        BigDecimal depositAmount = depositRequestDTO.getBalance().multiply(BigDecimal.valueOf(100));

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
    @RolesAllowed({"admin", "user"})
    public Response transferAccount(@Valid TransferAccountDTO transferRequestDTO){
        Account accountOrigin = Account.findById(transferRequestDTO.getUser_account_origin_id());
        if (accountOrigin == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Conta de origem não encontrada!").build();
        }
        Account accountDestiny = Account.findById(transferRequestDTO.getUser_account_destiny_id());
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

        log.info("Transação realizada: Origem - Banco: "
                + accountOrigin.getAgency_id().getBank_id().id +
                " Agência: " + accountOrigin.getAgency_id().id +
                " Conta: " + accountOrigin.id +
                " Destino - Banco: " + accountDestiny.getAgency_id().getBank_id().id +
                " Agência: " + accountDestiny.getAgency_id().id +
                " Conta: " + accountDestiny.id + " Valor: " + "R$" + transferValue.divide(BigDecimal.valueOf(100)) +
                " Data: " + java.time.LocalDate.now() + " Hora: " + java.time.LocalTime.now());


        return Response.status(200).entity("Transferência cocluída!").build();
    }



}
