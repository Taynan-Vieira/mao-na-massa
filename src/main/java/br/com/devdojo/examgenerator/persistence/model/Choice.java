package br.com.devdojo.examgenerator.persistence.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Choice extends AbstractEntity {
    @NotEmpty(message = "O nome título não pode estar vazio")
    @ApiModelProperty(notes = "O título da alternativa")
    private String title;
    @NotNull(message = "O nome campo correAnswer deve ser verdadeiro ou falso")
    @ApiModelProperty(notes = "Alternativa correta para a questão associada, você pode ter apenas uma questão correta por questão")
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean correctAnswer;
    @ManyToOne(optional = false)
    private Question question;
    @ManyToOne(optional = false)
    private Professor professor;

    public static final class Builder {
        private Choice choice;

        private Builder() {
            choice = new Choice();
        }

        public static Builder newChoice() {
            return new Builder();
        }

        public Builder id(long id) {
            choice.setId(id);
            return this;
        }

        public Builder enabled(boolean enabled) {
            choice.setEnabled(enabled);
            return this;
        }

        public Builder title(String title) {
            choice.setTitle(title);
            return this;
        }

        public Builder correctAnswer(boolean correctAnswer) {
            choice.setCorrectAnswer(correctAnswer);
            return this;
        }

        public Builder question(Question question) {
            choice.setQuestion(question);
            return this;
        }

        public Builder professor(Professor professor) {
            choice.setProfessor(professor);
            return this;
        }

        public Choice build() {
            return choice;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
