package br.com.devdojo.examgenerator.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
public class Professor extends  AbstractEntity {
    @NotEmpty(message = "O campo nome não pode estar vazio")
    private String name;
    @Email(message = "Este e-mail não é válido")
    @NotEmpty(message = "O campo e-mail não pode estar vazio")
    @Column(unique = true)
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static final class Builder {
        private Professor professor;

        private Builder() {
            professor = new Professor();
        }

        public static Builder newProfessor() {
            return new Builder();
        }

        public Builder name(String name) {
            professor.setName(name);
            return this;
        }

        public Builder id(long id) {
            professor.setId(id);
            return this;
        }

        public Builder email(String email) {
            professor.setEmail(email);
            return this;
        }

        public Professor build() {
            return professor;
        }
    }
}
