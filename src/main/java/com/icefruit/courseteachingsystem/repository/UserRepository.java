package com.icefruit.courseteachingsystem.repository;

import com.icefruit.courseteachingsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    int countByPhoneNumber(String phoneNumber);

    User findByPhoneNumber(String phoneNumber);

    User findById(long id);

}