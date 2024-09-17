package com.booleanuk.api.repository;

import com.booleanuk.api.models.LibraryUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryUserRepository extends JpaRepository<LibraryUser, Integer>{
}
