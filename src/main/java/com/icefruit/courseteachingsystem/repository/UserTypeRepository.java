package com.icefruit.courseteachingsystem.repository;

import com.icefruit.courseteachingsystem.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {
}