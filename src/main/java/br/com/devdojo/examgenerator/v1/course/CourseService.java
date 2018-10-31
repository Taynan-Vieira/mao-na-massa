package br.com.devdojo.examgenerator.v1.course;

import br.com.devdojo.examgenerator.exception.ResourceNotFoundException;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CourseService implements Serializable {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void throwResourceNotFoundIfCourseNotExist(Course course) {
        if (course.getId() == null || course.getName().trim().isEmpty())
            throw new ResourceNotFoundException("O nome do campo não pode ser vazio");

        if (course == null || course.getId() == null || courseRepository.findBy(course.getId()) == null)
            throw new ResourceNotFoundException("Course not found");
    }


/*
    public void throwFieldNameCanNotBeEmpty(Course name){


    }*/
}
