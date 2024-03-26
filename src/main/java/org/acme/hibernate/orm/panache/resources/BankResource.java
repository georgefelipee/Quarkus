package org.acme.hibernate.orm.panache.resources;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.hibernate.orm.panache.dto.CreateBankDTO;
import org.acme.hibernate.orm.panache.dto.CreateUserRequestDTo;
import org.acme.hibernate.orm.panache.exceptions.ResponseError;
import org.acme.hibernate.orm.panache.models.Bank;

import java.util.List;
import java.util.Set;

@Path("/banks")
@ApplicationScoped
@Consumes("application/json")
@Produces("application/json")
public class BankResource {

    @Inject
    Validator validator;

    @POST
    @Transactional
    public Response createBank(CreateBankDTO bankRequestDTO){
        Set<ConstraintViolation<CreateBankDTO>> violations = validator.validate(bankRequestDTO);

        if(!violations.isEmpty()){
            ResponseError responseError = ResponseError.createFromValidation(violations);
            return Response.status(Response.Status.BAD_REQUEST).entity(responseError).build();
        }

        Bank bank = new Bank();

        bank.setNameBank(bankRequestDTO.getNameBank());
        bank.persist();

        return Response.ok(bank).status(201).build();
    }

    @GET
    public Response getBanks() {
        PanacheQuery<PanacheEntityBase> query = Bank.findAll();
        return Response.ok(query.list()).status(200).build();
    }

    @GET
    @Path("/{id}")
    @Transactional
    public Response getBank(Long id){
        Bank bank = Bank.findById(id);
        if(bank == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Banco não encontrado!").build();
        }
        return Response.ok(bank).status(200).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteBank(@PathParam("id") Long id){
        Bank bank = Bank.findById(id);
        if(bank == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Banco não encontrado!").build();
        }
        bank.delete();
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateBank(@PathParam("id") Long id, CreateBankDTO bankRequestDTO){
        Bank bank = Bank.findById(id);
        if(bank == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Banco não encontrado!").build();
        }

        bank.setNameBank(bankRequestDTO.getNameBank());

        return Response.ok(bank).status(200).build();
    }


}
