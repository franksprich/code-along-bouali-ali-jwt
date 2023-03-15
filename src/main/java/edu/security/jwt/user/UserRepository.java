package edu.security.jwt.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * {DESCRIPTION}
 *
 * @author Frank Sprich
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}
