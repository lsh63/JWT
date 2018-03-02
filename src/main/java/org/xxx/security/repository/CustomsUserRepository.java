package org.xxx.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xxx.security.model.User;

/**
 * Created by stephan on 20.03.16.
 */
public interface CustomsUserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    
}
