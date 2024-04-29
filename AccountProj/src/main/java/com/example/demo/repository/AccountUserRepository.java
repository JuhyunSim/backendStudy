package com.example.demo.repository;

import com.example.demo.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountUserRepository
        extends JpaRepository<AccountUser, Long> {

}
