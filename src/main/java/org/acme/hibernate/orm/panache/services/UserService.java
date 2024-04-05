package org.acme.hibernate.orm.panache.services;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.acme.hibernate.orm.panache.User;
import org.acme.hibernate.orm.panache.dto.CreateUserRequestDTo;
import org.acme.hibernate.orm.panache.dto.LoginDTO;
import org.acme.hibernate.orm.panache.dto.UpdateUserDTO;
import org.acme.hibernate.orm.panache.dto.UserDTO;


import java.util.List;

import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {
    @Inject
    JwtServices jwtServices;


    public boolean isEmailExists(String email){
        PanacheQuery<PanacheEntityBase> query = User.find("email", email);
        return query.count() > 0;
    }

    public boolean isUsernameExists(String username){
        PanacheQuery<PanacheEntityBase> query = User.find("username", username);
        return query.count() > 0;
    }

    public User findByUsername(String username){
        return User.find("username", username).firstResult();
    }

    public User findByEmail(String email){
        return User.find("email", email).firstResult();
    }
    public User findByEmailOrUsername(String emailOrUsername){
        return User.find("email = ?1 or username = ?2", emailOrUsername, emailOrUsername).firstResult();
    }

    public User createUser(@Valid CreateUserRequestDTo userRequestDTo){

        User user = new User();
        user.setName(userRequestDTo.getName());
        user.setAge(userRequestDTo.getAge());

        user.setEmail(userRequestDTo.getEmail());
        user.setUsername(userRequestDTo.getUsername());

        String password = BcryptUtil.bcryptHash(userRequestDTo.getPassword());
        user.setPassword(password);

        if(isEmailExists(user.getEmail())) {
           throw new BadRequestException("Seu email já existe!");
        }
        if(isUsernameExists(user.getUsername())) {
            throw new BadRequestException("Seu username já existe!");
        }
        user.persist();
        return user;
    }

    public List<UserDTO> listAllUsers(){
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

        return userDTOs;
    }

    public void deleteuser(Long id){
        User user = User.findById(id);
        if(user == null){
            throw new BadRequestException("Usuário não encontrado!");
        }
        user.delete();
    }

    public UserDTO updateUser(Long id, UpdateUserDTO userRequestDTo){
        User user = User.findById(id);
        if(user == null){
            throw new NotFoundException("Usuário não encontrado!");
        }
        if(isEmailExists(userRequestDTo.getEmail())) {
            throw new BadRequestException("Esse email já está cadastrado!");
        }
        if(isUsernameExists(userRequestDTo.getUsername())) {
            throw new BadRequestException("Esse nome de usuário já está cadastrado!");
        }

        user.setName(userRequestDTo.getName());
        user.setAge(userRequestDTo.getAge());
        user.setEmail(userRequestDTo.getEmail());
        user.setUsername(userRequestDTo.getUsername());

        if(userRequestDTo.getPassword() != null){
            user.setPassword(BcryptUtil.bcryptHash(userRequestDTo.getPassword()));

        }

        user.persist();
        UserDTO userDTO = new UserDTO( user.getId(), user.getName(), user.getAge(), user.getEmail(), user.getUsername());

        return userDTO;
    }

    public String login(@Valid LoginDTO loginRequestDTO){
        if(loginRequestDTO.getEmail() == null && loginRequestDTO.getUsername() == null){
           throw new BadRequestException("Email ou nome de usuário é obrigatório!");
        }

        if(loginRequestDTO.getEmail() == null){
            User user = findByUsername(loginRequestDTO.getUsername());
            if(user == null){
                throw new NotFoundException("Usuário não encontrado!");
            }

            if(!BcryptUtil.matches(loginRequestDTO.getPassword(), user.getPassword())){
                throw new BadRequestException("Senha inválida!");
            }
            String token =  jwtServices.generateToken(user.getUsername());
            return token;

        } else {
            User user = findByEmail(loginRequestDTO.getEmail());
            if(user == null){
                throw new NotFoundException("Usuário não encontrado!");
            }
            boolean passwordCompare = BcryptUtil.matches(loginRequestDTO.getPassword(), user.getPassword());

            if(!passwordCompare){
                throw new BadRequestException("Senha inválida!");
            }
            String token =  jwtServices.generateToken(user.getUsername());
            return token;

        }
    }




}
