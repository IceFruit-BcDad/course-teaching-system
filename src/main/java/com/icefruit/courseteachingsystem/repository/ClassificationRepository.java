package com.icefruit.courseteachingsystem.repository;

import com.icefruit.courseteachingsystem.model.Classification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassificationRepository extends JpaRepository<Classification, Long> {

    Classification findById(long id);

    List<Classification> findClassificationsByLevel(int level);

    int countByName(String name);

    int deleteAllByParentIdIn(Iterable<Long> parentIds);

}