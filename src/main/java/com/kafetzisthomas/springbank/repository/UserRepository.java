package com.kafetzisthomas.springbank.repository;

import com.kafetzisthomas.springbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
