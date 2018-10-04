package br.com.devdojo.examgenerator.v1;

import br.com.devdojo.examgenerator.persistence.model.Professor;
import br.com.devdojo.examgenerator.persistence.repository.ProfessorRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("v1/professor")
public class ProfessorEndpoint {
    private final ProfessorRepository professorRepository;

    @Autowired
    public ProfessorEndpoint(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @GetMapping(path = "{id}")
    @ApiOperation(value = "Encontrar professor pelo ID", notes = "Temos que tornar este método melhor", response = Professor.class)
    public ResponseEntity<?> getProfessorById(@PathVariable long id) {
        Optional<Professor> professor = professorRepository.findById(id);
        return new ResponseEntity<>(professor, HttpStatus.OK);
    }
}
