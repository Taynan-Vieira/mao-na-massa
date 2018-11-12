package br.com.devdojo.examgenerator.persistence.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

@Entity
public class Question extends AbstractEntity {
    @NotEmpty(message = "O nome título não pode estar vazio")
    @ApiModelProperty(notes = "O título da questão")
    private String title;
    @ManyToOne
    private Course course;
    @ManyToOne
    private Professor professor;

    public static final class Builder {
        private Question question;

        private Builder() {
            question = new Question();
        }

        public static Builder newQuestion() {
            return new Builder();
        }

        public Builder id(long id) {
            question.setId(id);
            return this;
        }

        public Builder enabled(boolean enabled) {
            question.setEnabled(enabled);
            return this;
        }

        public Builder title(String title) {
            question.setTitle(title);
            return this;
        }

        public Builder course(Course course) {
            question.setCourse(course);
            return this;
        }

        public Builder professor(Professor professor) {
            question.setProfessor(professor);
            return this;
        }

        public Question build() {
            return question;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
