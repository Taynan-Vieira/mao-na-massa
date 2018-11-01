package br.com.devdojo.examgenerator.persistence.repository;

import br.com.devdojo.examgenerator.persistence.model.Course;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuestionRepository extends CustomPagingAndSortRepository<Course, Long> {
    @Query("select c from Course c where c.name like %?1% and c.professor = ?#{principal.professor} and c.enabled = true")
    List<Course> listCoursesByName(String name);

    @Transactional
    @Modifying
    @Query("delete from Course  c where c.id = ?1 and c.professor = ?#{principal.professor}")
    void deleteById(Long id);

    @Transactional
    @Modifying
    @Query("delete from Course c where c = ?1 and c.professor = ?#{principal.professor}")
    void delete(Course course);

}