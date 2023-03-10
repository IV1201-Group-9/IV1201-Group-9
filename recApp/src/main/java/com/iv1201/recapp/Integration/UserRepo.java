package com.iv1201.recapp.Integration;

import com.iv1201.recapp.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repository for User.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findUserByUsername(String principalName);

    List<User> findAllByApplicationStatusNotNullAndFirstnameNotNullOrderById();

    User findUserById(Long userId);
}
