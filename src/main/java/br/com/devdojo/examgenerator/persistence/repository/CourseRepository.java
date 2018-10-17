package br.com.devdojo.examgenerator.persistence.repository;

import br.com.devdojo.examgenerator.persistence.model.Course;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {
    @Query("select c from Course c where  c.id = ?1 and c.professor = ?#{principal.professor}")
//    Optional<Course> findById(Long id);
    Course findBy(Long id);

    @Query("select c from Course c where c = ?1 and c.professor = ?#{principal.professor}")
    Course findBy(Course course);

    @Query("select c from Course c where c.name like %?1% and c.professor = ?#{principal.professor}")
    List<Course> listCourses(String name);

    @Transactional
    @Modifying
    @Query("delete from Course  c where c.id = ?1 and c.professor = ?#{principal.professor}")
    void delete(Long id);

    @Transactional
    @Modifying
    @Query("delete from Course c where c = ?1 and c.professor = ?#{principal.professor}")
    void delete(Course course);

}