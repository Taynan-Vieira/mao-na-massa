package br.com.devdojo.examgenerator.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Entity
public class ApplicationUser extends AbstractEntity {
    @NotEmpty(message = "O campo usuário não pode ser vazio")
    @Column(unique=true)
    private String username;
    @NotEmpty(message = "O campo password não pode ser vazio")
    private String password;
    @OneToOne
    private Professor professor;

    public ApplicationUser() {
    }

    public ApplicationUser(ApplicationUser applicationUser) {
        this.username = applicationUser.username;
        this.password = applicationUser.password;
        this.professor = applicationUser.professor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ApplicationUser that = (ApplicationUser) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(professor, that.professor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, password, professor);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    @Override
    public String toString() {
        return "ApplicationUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", professor=" + professor +
                '}';
    }
}
