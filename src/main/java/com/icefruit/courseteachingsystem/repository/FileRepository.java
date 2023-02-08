package com.icefruit.courseteachingsystem.repository;

import com.icefruit.courseteachingsystem.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FileRepository extends JpaRepository<File, String> {
    File findByFilename(String filename);

    @Modifying(clearAutomatically = true)
    @Query("update File file set file.used = :used where file.id = :id")
    @Transactional
    int updateUsedById(@Param("used") String used, @Param("id") String id);
}