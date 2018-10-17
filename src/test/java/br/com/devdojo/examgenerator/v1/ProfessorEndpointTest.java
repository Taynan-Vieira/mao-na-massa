package br.com.devdojo.examgenerator.v1;

import br.com.devdojo.examgenerator.persistence.model.Professor;
import org.junit.Before;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class ProfessorEndpointTest {
    public static Professor mockProfessor() {
        return Professor.Builder.newProfessor()
                .id(1L)
                .name("tay")
                .email("tay@hotmail.com")
                .build();
    }

}