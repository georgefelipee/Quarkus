package org.acme.hibernate.orm.panache.resources;



import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.hibernate.orm.panache.dto.AgencyDTO;
import org.acme.hibernate.orm.panache.dto.CreateAgencyDTO;

import jakarta.validation.Validator;
import org.acme.hibernate.orm.panache.models.Agency;
import org.acme.hibernate.orm.panache.services.AgencyServices;

import java.util.List;

@Path("/agencies")
@ApplicationScoped
@Consumes("application/json")
@Produces("application/json")
public class AgencyResource {

    @Inject
    AgencyServices agencyServices;

    @POST
    @Transactional
    public Response createAgency(@Valid CreateAgencyDTO agencyRequestDTO){
        Agency agency = agencyServices.createAgency(agencyRequestDTO);
        AgencyDTO agencyDTO = new AgencyDTO(agency);

        return Response.ok().entity(agencyDTO).status(201).build();
    }

    // Métodos GET para recuperar agências
    @GET
    public Response getAgencies() {
       List<AgencyDTO> agencyDTO = agencyServices.listAllAgencies();

        return Response.ok(agencyDTO).status(200).build();
    }
    @GET
    @Path("/bank/{bankId}")
    public Response getAgencyByBank( Long bankId ){
        List<AgencyDTO> agencyDTO = agencyServices.listAllAgenciesByBank(bankId);
        return Response.ok(agencyDTO).status(200).build();
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
    public Response updateAgency(@PathParam("id") Long id, CreateAgencyDTO agencyRequestDTO){
       Agency agency = agencyServices.updateAgency(id, agencyRequestDTO);
       AgencyDTO agencyDTO = new AgencyDTO(agency);
       return Response.ok().entity(agencyDTO).status(200).build();
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
