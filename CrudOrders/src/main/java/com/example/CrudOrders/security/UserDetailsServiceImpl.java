package com.example.CrudOrders.security;

import java.util.List;
import java.util.Set;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

                               //Carga usuario//
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var usuario = getById(username); //Busca usuario por name//

        if (usuario == null) {
            throw new UsernameNotFoundException(username); //Si usuario no se encuentra, manda excepción//
        }

        return User //Construye y devuelve un objeto con datos usuario//
                .withUsername(username)
                .password(usuario.password())
                .roles(usuario.roles().toArray(new String[0]))
                .build();
    }

    public record Usuario(String username, String password, Set<String> roles) {};

                                //Busca por ID//
    public static Usuario getById(String username) {

        var password = "$2a$10$becI/hHawgrW4U6YE788k.LkN6QpQAgtcUwBwxSyNIof7mOzbZS7K"; //Password hasheada//

        Usuario rodrigo = new Usuario(
                "rodrigo",
                password,
                Set.of("USER") //Crea usuario de ej//
        );

        var usuarios = List.of(rodrigo); // lista de usuarios//

        return usuarios  // Busca y devuelve usuario que coincida con nombre usuario//
                .stream()
                .filter(e -> e.username().equals(username))
                .findFirst()
                .orElse(null);
    }

}

