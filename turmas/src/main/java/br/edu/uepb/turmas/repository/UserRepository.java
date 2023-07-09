package br.edu.uepb.turmas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.uepb.turmas.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    boolean findByauthority(String authority);
    
}