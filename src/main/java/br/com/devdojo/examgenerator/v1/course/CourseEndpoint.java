package br.com.devdojo.examgenerator.v1.course;

import br.com.devdojo.examgenerator.persistence.model.ApplicationUser;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("v1/professor/course")
@Api(description = "Operações relacionadas aos professores do curso")
public class CourseEndpoint {
    private final CourseRepository courseRepository;
    private EndpointUtil endpointUtil = new EndpointUtil();

    @Autowired
    public CourseEndpoint(CourseRepository courseRepository, EndpointUtil endpointUtil) {
        this.courseRepository = courseRepository;
        this.endpointUtil = endpointUtil;
    }


    @ApiOperation(value = "Retornar um curso com base em s eu id", response = Course.class)
    @GetMapping(path = "{id}")
    public ResponseEntity<?> getCourseById(@PathVariable long id) {
        return endpointUtil.returnObjectOrNotFound(courseRepository.findById(id));

        /*Optional<Course> course = courseRepository.findById(id);
        if(!course.isPresent()) //Importante
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);*/

    }

    @ApiOperation(value = "Retornar a lista dos cursos  relacionados ao professor", response = Course.class)
    @GetMapping(path = "list")
    public ResponseEntity<?> listCourses(@ApiParam("Course name") @RequestParam(value = "name", defaultValue = "") String name) {
        return endpointUtil.returnObjectOrNotFound(courseRepository.listCourses(name));

    }

    @ApiOperation(value = "Deletar o curso especificado e retonar 200 Ok sem o corpo")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (!course.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            courseRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    /*@ApiOperation(value = "Atualizar curso especificado e retonar 200 Ok sem o corpo")
    @PutMapping
    public ResponseEntity<?> delete(@RequestBody Course course) {
        Optional<Course>  Course = courseRepository.findAll(course);
        if(!course.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            courseRepository.save();
        return new ResponseEntity<>(HttpStatus.OK);
    }*/

    @ApiOperation(value = "Criar o curso especificado e retonar o curso criado")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Course course) {
        course.setProfessor(endpointUtil.extractProfessorFromToken());
        return new ResponseEntity<>(courseRepository.save(course), HttpStatus.OK);
    }

}