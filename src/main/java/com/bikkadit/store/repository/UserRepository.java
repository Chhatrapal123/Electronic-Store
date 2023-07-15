package com.bikkadit.store.repository;

import com.bikkadit.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String>
{
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email,String Password);
    List<User> findByNameContaining(String keywords);
}
