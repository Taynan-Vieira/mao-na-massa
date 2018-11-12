package br.com.devdojo.examgenerator.persistence.repository;

import br.com.devdojo.examgenerator.persistence.model.Question;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuestionRepository extends CustomPagingAndSortRepository<Question, Long> {
    @Query("select q from Question q where q.course.id =?1 and q.title like %?2% and q.professor = ?#{principal.professor} and q.enabled = true")
    List<Question> listQuestionsByCourseAndTitle(long courseId, String title);

    @Transactional
    @Modifying
    @Query("delete from Question q where q.id = ?1 and q.professor = ?#{principal.professor}")
    void deleteById(Long id);

    @Transactional
    @Modifying
    @Query("delete from Question q where q = ?1 and q.professor = ?#{principal.professor}")
    void delete(Question Question);

}