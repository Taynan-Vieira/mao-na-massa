package br.com.devdojo.examgenerator.endpoint.v1.course;

import br.com.devdojo.examgenerator.endpoint.v1.deleteservice.CascadeDeleteService;
import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
//import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("v1/professor/course")
@Api(description = "Operações relacionadas aos professores do curso")
public class CourseEndpoint {
    private final CourseRepository courseRepository;
    private final QuestionRepository questionRepository;
    private final GenericService service;
    private final CascadeDeleteService deleteService;
    private EndpointUtil endpointUtil;

    @Autowired
    public CourseEndpoint(CourseRepository courseRepository, QuestionRepository questionRepository, GenericService service, CascadeDeleteService deleteService, EndpointUtil endpointUtil) {
        this.courseRepository = courseRepository;
        this.questionRepository = questionRepository;
        this.service = service;
        this.deleteService = deleteService;
        this.endpointUtil = endpointUtil;
    }


    @ApiOperation(value = "Retornar um curso com base em s eu id", response = Course.class)
    @GetMapping(path = "{id}")
    public ResponseEntity<?> getCourseById(@PathVariable long id) {
        return endpointUtil.returnObjectOrNotFound(courseRepository.findBy(id));
    }

    @ApiOperation(value = "Retornar data de criação dos cursos", response = Course.class)
    @GetMapping(path = "listDateCreationCourse/{id}")
    public ResponseEntity<?> listDateCreationCourses(@RequestParam(value = "dateCreation", defaultValue = "") LocalDate listDateCreation) {
        return new ResponseEntity<>(courseRepository.listDateCreationCourses(listDateCreation), HttpStatus.OK);
    }

    @ApiOperation(value = "Retornar data de eventos dos cursos", response = Course.class)
    @GetMapping(path = "listDateEventCourse/{id}")
    public ResponseEntity<?> listDateEventCourses(@RequestParam(value = "dateEvent", defaultValue = "") LocalDate listDateEvent) {
        return new ResponseEntity<>(courseRepository.listDateEventCourses(listDateEvent), HttpStatus.OK);
    }

    @ApiOperation(value = "Retornar a lista dos cursos  relacionados ao professor", response = Course.class)
    @GetMapping(path = "list")
    public ResponseEntity<?> listCourses(@ApiParam("Course name") @RequestParam(value = "name", defaultValue = "") String name) {
        return new ResponseEntity<>(courseRepository.listCoursesByName(name), HttpStatus.OK);
    }

    @ApiOperation(value = "Deletar o curso especificado e todas as questões relacionada e suas alternativas retonar 200 Ok")
    @DeleteMapping(path = "{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable long id) {
        validateCourseExistence(id);
        deleteService.cascadeDeleteCourseQuestionAndChoice(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateCourseExistence(@PathVariable long id) {
        validateQuestionExistenceOnDB(id);
    }

    /********************************************/
    @ApiOperation(value = "Atualizar o curso especificado e retonar 200 Ok")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Course course) {
        validateQuestionExistenceOnDB(course.getId());
        return new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK);
    }

    private void validateQuestionExistenceOnDB(Long id) {
        service.throwResourceNotFoundIfDoesNotExist(id, courseRepository, "Course not found");
    }

    @ApiOperation(value = "Criar o curso especificado e retonar o curso criado")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Course course) {
        course.setProfessor(endpointUtil.extractProfessorFromToken());
        return new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK);
    }

    /*private void validateCoursesQuestion(@RequestBody @Valid Course course) {
        service.throwResourceNotFoundIfDoesNotExist(course.getName(), courseRepository, "The question related to this choice was not found");
    }*/

}