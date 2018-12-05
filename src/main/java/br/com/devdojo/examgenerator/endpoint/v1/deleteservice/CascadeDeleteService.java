package br.com.devdojo.examgenerator.endpoint.v1.deleteservice;

import br.com.devdojo.examgenerator.persistence.repository.ChoiceRepository;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CascadeDeleteService {
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public CascadeDeleteService(QuestionRepository questionRepository, ChoiceRepository choiceRepository, CourseRepository courseRepository) {
        this.questionRepository = questionRepository;
        this.choiceRepository = choiceRepository;
        this.courseRepository = courseRepository;
    }

    public void cascadeDeleteCourseQuestionAndChoice(long courseId){
        courseRepository.deleteById(courseId);
        questionRepository.deleteAllQuestionRelatedToCourse(courseId);
        choiceRepository.deleteAllChoicesRelatedToCourse(courseId);

    }public void cascadeDeleteQuestionAndChoice(long questionId){
        courseRepository.deleteById(questionId);
        choiceRepository.deleteAllChoicesRelatedToQuestion(questionId);
    }
}
