package com.icefruit.courseteachingsystem.repository;

import com.icefruit.courseteachingsystem.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    List<Chapter> findAllByCourseId(long courseId);

    Chapter findById(long id);
}