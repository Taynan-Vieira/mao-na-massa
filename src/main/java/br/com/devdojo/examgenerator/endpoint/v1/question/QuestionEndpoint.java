package br.com.devdojo.examgenerator.endpoint.v1.question;

import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("v1/professor/course/question")
@Api(description = "Operações relacionadas as questões do curso")
public class QuestionEndpoint {
    private final QuestionRepository questionRepository;
    private final CourseRepository courseRepository;
    private final GenericService service;
    private EndpointUtil endpointUtil;

    @Autowired
    public QuestionEndpoint(QuestionRepository questionRepository, CourseRepository courseRepository, GenericService service, EndpointUtil endpointUtil) {
        this.questionRepository = questionRepository;
        this.courseRepository = courseRepository;
        this.service = service;
        this.endpointUtil = endpointUtil;
    }


    @ApiOperation(value = "Retorna uma questão com base em seu id", response = Question.class)
    @GetMapping(path = "{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable long id) {
        return endpointUtil.returnObjectOrNotFound(questionRepository.findBy(id));
    }

    @ApiOperation(value = "Retorna a lista das questões relacionadas ao curso", response = Question.class)
    @GetMapping(path = "list/{courseId}/")
    public ResponseEntity<?> listQuestions (@PathVariable long courseId, @ApiParam("title") @RequestParam(value = "title", defaultValue = "") String name) {
        return new ResponseEntity<>(questionRepository.listQuestionsByCourseAndTitle(courseId, name), HttpStatus.OK);

    }

    @ApiOperation(value = "Deletar o curso especificado e retonar 200 Ok")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        validateQuestionExistenceOnDB(id);
        questionRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /********************************************/
    @ApiOperation(value = "Atualizar o curso especificado e retonar 200 Ok")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Question question) {
        validateQuestionExistenceOnDB(question.getId());
        questionRepository.save(question);
        return new ResponseEntity<>(questionRepository.save(question), HttpStatus.OK);
    }

    private void validateQuestionExistenceOnDB(Long id) {
        service.throwResourceNotFoundIfDoesNotExist(id, questionRepository, "Question not found");
    }

    /********************************************/

    @ApiOperation(value = "Criar a questão especificada e retonar a questão criada")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Question question) {
        service.throwResourceNotFoundIfDoesNotExist(question.getCourse(), courseRepository, "Course not found");
        question.setProfessor(endpointUtil.extractProfessorFromToken());
        return new ResponseEntity<>(questionRepository.save(question), HttpStatus.OK);
    }

}