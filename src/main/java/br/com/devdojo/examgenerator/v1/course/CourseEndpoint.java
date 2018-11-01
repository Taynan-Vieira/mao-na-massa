package br.com.devdojo.examgenerator.v1.course;

import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
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
@RequestMapping("v1/professor/course")
@Api(description = "Operações relacionadas aos professores do curso")
public class CourseEndpoint {
    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private EndpointUtil endpointUtil;

    @Autowired
    public CourseEndpoint(CourseRepository courseRepository, CourseService courseService, EndpointUtil endpointUtil) {
        this.courseRepository = courseRepository;
        this.courseService = courseService;
        this.endpointUtil = endpointUtil;
    }


    @ApiOperation(value = "Retornar um curso com base em s eu id", response = Course.class)
    @GetMapping(path = "{id}")
    public ResponseEntity<?> getCourseById(@PathVariable long id) {
        return endpointUtil.returnObjectOrNotFound(courseRepository.findBy(id));
        /*Optional<Course> course = courseRepository.findById(id);
        if(!course.isPresent()) //Importante
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);*/
    }

    @ApiOperation(value = "Retornar a lista dos cursos  relacionados ao professor", response = Course.class)
    @GetMapping(path = "list")
    public ResponseEntity<?> listCourses(@ApiParam("Course name") @RequestParam(value = "name", defaultValue = "") String name) {
        return new ResponseEntity<>(courseRepository.listCoursesByName(name), HttpStatus.OK);

    }

    @ApiOperation(value = "Deletar o curso especificado e retonar 200 Ok")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        courseService.throwResourceNotFoundIfCourseNotExist(courseRepository.findBy(id));
        courseRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /********************************************/
    @ApiOperation(value = "Atualizar o curso especificado e retonar 200 Ok")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Course course) {
        courseService.throwResourceNotFoundIfCourseNotExist(courseRepository.findBy(course));
        return new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK);
    }
    /********************************************/

    @ApiOperation(value = "Criar o curso especificado e retonar o curso criado")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Course course) {
        course.setProfessor(endpointUtil.extractProfessorFromToken());
        return new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK);
    }

}