package com.kafetzisthomas.springbank.security;

import com.kafetzisthomas.springbank.entity.Authority;
import com.kafetzisthomas.springbank.entity.User;
import com.kafetzisthomas.springbank.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class JpaUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;

    public JpaUserDetailsManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(UserDetails userDetails) {
        User user = new User(userDetails.getUsername(), userDetails.getPassword(), userDetails.isEnabled());
        for (GrantedAuthority auth : userDetails.getAuthorities()) {
            user.addAuthority(new Authority(user, auth.getAuthority()));
        }
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("updateUser not implemented");
    }

    @Override
    public void deleteUser(String email) {
        userRepository.deleteById(email);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("changePassword not implemented");
    }

    @Override
    public boolean userExists(String email) {
        return userRepository.existsById(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true, true, true,
                user.getAuthorities().stream()
                        .map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
                        .collect(Collectors.toList())
        );
    }
}
