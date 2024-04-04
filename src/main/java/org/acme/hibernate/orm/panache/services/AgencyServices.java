package org.acme.hibernate.orm.panache.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.acme.hibernate.orm.panache.dto.AgencyDTO;
import org.acme.hibernate.orm.panache.dto.CreateAgencyDTO;
import org.acme.hibernate.orm.panache.exceptions.ErrorResponseEdit;
import org.acme.hibernate.orm.panache.models.Agency;
import org.acme.hibernate.orm.panache.models.Bank;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AgencyServices {


    public Agency createAgency(CreateAgencyDTO agencyRequestDTO) {
        Bank bank = Bank.findById(agencyRequestDTO.getBank_id());
        if (bank == null) {
            throw new NotFoundException("Banco não encontrado");
        }

        Agency agency = new Agency();
        agency.setNameAgency(agencyRequestDTO.getNameAgency()); // Supondo que o DTO tenha um método getName() para obter o nome da agência
        agency.setBank_id(bank);
        agency.persist();

        return agency;

    }

    public List<AgencyDTO> listAllAgencies() {
        List<Agency> agencies = Agency.findAll().list();
        List<AgencyDTO> agenciesDTO = agencies.stream()
                .map(AgencyDTO::new).toList();

        return agenciesDTO;

    }

    public Agency updateAgency(Long id, CreateAgencyDTO agencyRequestDTO) {
        Agency agency = Agency.findById(id);
        if (agency == null) {
            throw new NotFoundException("Agência não encontrada");
        }

        if(agencyRequestDTO.getNameAgency() == null || agencyRequestDTO.getNameAgency().isEmpty()){
            throw new  BadRequestException("Nome da agência não pode ser vazio");
        }

        Bank bank = Bank.findById(agencyRequestDTO.getBank_id());
        if (bank == null) {
            throw new NotFoundException("Banco não encontrado");
        }

        agency.setNameAgency(agencyRequestDTO.getNameAgency());
        agency.setBank_id(bank);
        agency.persist();

        return agency;
    }
}
