package br.com.devdojo.examgenerator.persistence.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

@Entity
public class Course extends AbstractEntity {

    @NotEmpty(message = "O nome do curso não pode estar vazio")
    @ApiModelProperty(notes = "O nome do curso")
    private String name;
    @ManyToOne(optional = false)
    private Professor professor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public static final class Builder {
        private Course course;

        private Builder() {
            course = new Course();
        }

        public static Builder newCourse() {
            return new Builder();
        }

        public Builder withName(String name) {
            course.setName(name);
            return this;
        }

        public Builder withId(long id) {
            course.setId(id);
            return this;
        }

        public Builder withProfessor(Professor professor) {
            course.setProfessor(professor);
            return this;
        }

        public Course build() {
            return course;
        }
    }
}
