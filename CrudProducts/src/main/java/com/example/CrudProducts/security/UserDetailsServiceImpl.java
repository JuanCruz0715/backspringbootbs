package com.example.CrudProducts.security;

import java.util.List;
import java.util.Set;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /* user */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var usuario = getById(username);

        if (usuario == null) {
            throw new UsernameNotFoundException(username); //
        }

        return User
                .withUsername(username)
                .password(usuario.password())
                .roles(usuario.roles().toArray(new String[0]))
                .build();
    }

    public record Usuario(String username, String password, Set<String> roles) {};

    /* map id*/
    public static Usuario getById(String username) {

        var password = "$2a$10$/1RfwrjmJKWQyQ6w96SHn.2xBvyssKV3Dq4qwoow7TQcId8Y8M4La";

        Usuario User = new Usuario(
                "jotaU",
                password,
                Set.of("USER")
        );

        var usuarios = List.of(User);

        return usuarios
                .stream()
                .filter(e -> e.username().equals(username))
                .findFirst()
                .orElse(null);
    }

}

