package br.com.devdojo.examgenerator.endpoint.v1.course;

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
    private EndpointUtil endpointUtil;

    @Autowired
    public CourseEndpoint(CourseRepository courseRepository, QuestionRepository questionRepository, GenericService service, EndpointUtil endpointUtil) {
        this.courseRepository = courseRepository;
        this.questionRepository = questionRepository;
        this.service = service;
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

    @ApiOperation(value = "Deletar o curso especificado e retonar 200 Ok")
    @DeleteMapping(path = "{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable long id) {
        service.throwResourceNotFoundIfDoesNotExist(id, courseRepository, "Course not found");
        questionRepository.deleteAllQuestionRelatedToCourse(id);
        courseRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /********************************************/
    @ApiOperation(value = "Atualizar o curso especificado e retonar 200 Ok")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Course course) {
        service.throwResourceNotFoundIfDoesNotExist(course, courseRepository, "Course not found");
        return new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK);
    }

    @ApiOperation(value = "Criar o curso especificado e retonar o curso criado")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Course course) {
        course.setProfessor(endpointUtil.extractProfessorFromToken());
        return new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK);
    }

}