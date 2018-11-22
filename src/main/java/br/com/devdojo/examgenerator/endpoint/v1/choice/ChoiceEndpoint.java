package br.com.devdojo.examgenerator.endpoint.v1.choice;

import br.com.devdojo.examgenerator.endpoint.v1.genericservice.GenericService;
import br.com.devdojo.examgenerator.persistence.model.Choice;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.repository.ChoiceRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;
import br.com.devdojo.examgenerator.util.EndpointUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("v1/professor/course/question/choice")
@Api(description = "Operações relacionadas as alternativas da questão")
public class ChoiceEndpoint {
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;
    private final GenericService service;
    private EndpointUtil endpointUtil;

    @Autowired
    public ChoiceEndpoint(QuestionRepository questionRepository, ChoiceRepository choiceRepository, GenericService service, EndpointUtil endpointUtil) {
        this.questionRepository = questionRepository;
        this.choiceRepository = choiceRepository;
        this.service = service;
        this.endpointUtil = endpointUtil;
    }

    @ApiOperation(value = "Retorna uma alternativa com base em seu id", response = Choice.class)
    @GetMapping(path = "{id}")
    public ResponseEntity<?> getChoiceById(@PathVariable long id) {
        return endpointUtil.returnObjectOrNotFound(choiceRepository.findBy(id));
    }

    @ApiOperation(value = "Retorna a lista das alternativas relacionadas a questão", response = Choice[].class)
    @GetMapping(path = "list/{questionId}/")
    public ResponseEntity<?> listChoicesByQuestionId(@PathVariable long questionId) {
        return new ResponseEntity<>(choiceRepository.listChoicesByQuestionId(questionId), HttpStatus.OK);

    }

    @ApiOperation(value = "Criar a alternativa especificada e retonar a alternativa criada", notes = "Se a resposta correta da questão for verdadeira, todas as outras opções serão falsas. A resposta relacionada a essa pergunta será atualizada para falsa")
    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@Valid @RequestBody Choice choice) {
        validateChoicesQuestion(choice);
        choice.setProfessor(endpointUtil.extractProfessorFromToken());
        Choice savedChoice = choiceRepository.save(choice);
        updateChangingOtherChoicesCorrectAnswerToFalse(choice);
        return new ResponseEntity<>(savedChoice, HttpStatus.OK);
    }

    @ApiOperation(value = "Atualizar a alternativa especificada e retonar 200 OK", notes = "Se a resposta correta da questão for verdadeira, todas as outras opções serão falsas. A resposta relacionada a essa pergunta será atualizada para falsa")
    @PutMapping
    @Transactional
    public ResponseEntity<?> update(@Valid @RequestBody Choice choice) {
        validateChoicesQuestion(choice);
        updateChangingOtherChoicesCorrectAnswerToFalse(choice);
        choiceRepository.save(choice);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Deletar a alternativa especificada e retonar 200 Ok")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        service.throwResourceNotFoundIfDoesNotExist(id, choiceRepository, "Choice was not found");
        choiceRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateChoicesQuestion(@RequestBody @Valid Choice choice) {
        service.throwResourceNotFoundIfDoesNotExist(choice.getQuestion(), questionRepository, "The question related to this choice was not found");
    }

    private void updateChangingOtherChoicesCorrectAnswerToFalse(Choice choice) {
        if (choice.isCorrectAnswer())
            choiceRepository.updateAllOtherChoicesCorrectAnswerToFalse(choice, choice.getQuestion());
    }
}
