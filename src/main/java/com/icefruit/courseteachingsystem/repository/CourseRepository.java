package com.icefruit.courseteachingsystem.repository;

import com.icefruit.courseteachingsystem.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    int countByNameAndClassificationId(String name, long classificationId);

    Course findById(long id);

    Page<Course> findAllByClassificationId(long classificationId, Pageable pageable);
}