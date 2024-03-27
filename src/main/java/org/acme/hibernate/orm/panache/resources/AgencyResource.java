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
import org.acme.hibernate.orm.panache.dto.CreateAgencyDTO;

import jakarta.validation.Validator;
import org.acme.hibernate.orm.panache.exceptions.ErrorResponseEdit;
import org.acme.hibernate.orm.panache.exceptions.ResponseError;
import org.acme.hibernate.orm.panache.models.Agency;
import org.acme.hibernate.orm.panache.models.Bank;

import java.util.Set;

@Path("/agencies")
@ApplicationScoped
@Consumes("application/json")
@Produces("application/json")
public class AgencyResource {

    @Inject
    Validator validator;

    @POST
    @Transactional
    public Response createAgency(@Valid CreateAgencyDTO agencyRequestDTO){

        try {
            Bank bank = Bank.findById(agencyRequestDTO.getBank_id());
            if (bank == null) {
                ErrorResponseEdit errorResponse = new ErrorResponseEdit( "Banco não encontrado!");
                return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
            }

            Agency agency = new Agency();
            agency.setNameAgency(agencyRequestDTO.getNameAgency()); // Supondo que o DTO tenha um método getName() para obter o nome da agência
            agency.setBank_id(bank);
            agency.persist();


            // Retornar uma resposta de sucesso
        } catch (Exception e) {
            // Logar detalhes sobre a exceção para depuração
            e.printStackTrace();

            // Retornar uma resposta de erro genérica
            ErrorResponseEdit errorResponse = new ErrorResponseEdit( "Erro interno do servidor");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }

        return Response.ok().status(201).build();
    }

    // Métodos GET para recuperar agências
    @GET
    public Response getAgencies() {
        PanacheQuery<PanacheEntityBase> query = Agency.findAll();
        return Response.ok(query.list()).status(200).build();
    }

    @GET
    @Path("/{id}")
    @Transactional
    public Response getAgency(Long id) {
        Agency agency = Agency.findById(id);
        if (agency == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Agência não encontrada!").build();
        }
        return Response.ok(agency).status(200).build();

    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response editAgency(@PathParam("id") Long id, CreateAgencyDTO agencyRequestDTO){
        Agency agency = Agency.findById(id);
        if(agency == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Agência não encontrada!").build();
        }

        if(agencyRequestDTO.getBank_id() == -1){
            agency.setNameAgency(agencyRequestDTO.getNameAgency());
            return Response.ok(agency).status(200).build();
        } else{
            agency.setNameAgency(agencyRequestDTO.getNameAgency());
            agency.setBank_id(Bank.findById(agencyRequestDTO.getBank_id()));
            return Response.ok(agency).status(200).build();
        }

    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteAgency(@PathParam("id") Long id){
        Agency agency = Agency.findById(id);
        if(agency == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Agência não encontrada!").build();
        }
        agency.delete();
        return Response.status(Response.Status.NO_CONTENT).build();
    }



}
