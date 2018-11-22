package br.com.devdojo.examgenerator.endpoint.v1.choice;

import br.com.devdojo.examgenerator.endpoint.v1.ProfessorEndpointTest;
import br.com.devdojo.examgenerator.endpoint.v1.question.QuestionEndpointTest;
import br.com.devdojo.examgenerator.persistence.model.Choice;
import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.model.Question;
import br.com.devdojo.examgenerator.persistence.repository.ChoiceRepository;
import br.com.devdojo.examgenerator.persistence.repository.ProfessorRepository;
import br.com.devdojo.examgenerator.persistence.repository.QuestionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ChoiceEndpointTest {
    @MockBean
    private QuestionRepository questionRepository;
    @MockBean
    private ChoiceRepository choiceRepository;
    @MockBean
    private ProfessorRepository professorRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;
    private HttpEntity<Void> professorHeader;
    private HttpEntity<Void> wrongHeader;
    private Choice choiceCorrectAnswerFalse = mockChoiceCorrectAnswerFalse();
    private Choice choiceCorrectAnswerTrue = mockChoiceCorrectAnswerTrue();

    private static Choice mockChoiceCorrectAnswerFalse() {
        return Choice.Builder.newChoice()
                .id(1L)
                .title("Is a room")
                .question(QuestionEndpointTest.mockQuestion())
                .correctAnswer(false)
                .professor(ProfessorEndpointTest.mockProfessor())
                .build();
    }

    private static Choice mockChoiceCorrectAnswerTrue() {
        return Choice.Builder.newChoice()
                .id(2L)
                .title("Is a template?")
                .question(QuestionEndpointTest.mockQuestion())
                .correctAnswer(false)
                .professor(ProfessorEndpointTest.mockProfessor())
                .build();
    }


    @Before
    public void configProfessorHeader() {
        String body = "{\"username\":\"taynan\",\"password\":\"devdojo\"}";
        HttpHeaders headers = testRestTemplate.postForEntity("/login", body, String.class).getHeaders();
        this.professorHeader = new HttpEntity<>(headers);
    }

    @Before
    public void configWrongHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "1111");
        this.wrongHeader = new HttpEntity<>(headers);
    }

    @Before
    public void setup() {
        BDDMockito.when(choiceRepository.findBy(choiceCorrectAnswerFalse.getId())).thenReturn(choiceCorrectAnswerFalse);
        BDDMockito.when(choiceRepository.findBy(choiceCorrectAnswerTrue.getId())).thenReturn(choiceCorrectAnswerTrue);
        BDDMockito.when(choiceRepository.listChoicesByQuestionId(choiceCorrectAnswerTrue.getQuestion().getId())).thenReturn(asList(choiceCorrectAnswerFalse, choiceCorrectAnswerTrue));
        BDDMockito.when(questionRepository.findBy(choiceCorrectAnswerTrue.getQuestion().getId()))
                .thenReturn(choiceCorrectAnswerTrue.getQuestion());

    }

    @Test
    public void getChoiceByIdWhenTokenIsWrongShouldReturn403() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/1", HttpMethod.GET, wrongHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listChoiceByQuestionIdWhenTokenIsWrongShouldReturn403() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/list/1/", HttpMethod.GET, wrongHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listChoicesByQuestionIdWhenQuestionIdDoesNotExistsShouldReturnEmptyList() throws Exception {
        ResponseEntity<List<Choice>> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/list/99/", HttpMethod.GET, professorHeader, new ParameterizedTypeReference<List<Choice>>() {
        });
        assertThat(exchange.getBody()).isEmpty();
    }

    @Test
    public void listChoicesByQuestionIdWhenQuestionIdExistsShouldReturn200() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/list/1/", HttpMethod.GET, professorHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getChoiceByIdWithouthIdShouldReturn400() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/", HttpMethod.GET, professorHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void getChoiceByIdWhenChoiceIdDoesNotExistsShouldReturn404() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/-1", HttpMethod.GET, professorHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void getChoiceByIdWhenChoiceIdExitsShouldReturn200() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/1", HttpMethod.GET, professorHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void deleteChoiceWhenIdExistsShouldReturn200() throws Exception {
        long id = 1L;
        BDDMockito.doNothing().when(choiceRepository).deleteById(id);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/{id}", HttpMethod.DELETE, professorHeader, String.class, id);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void deleteWhenIdDoesNotExistsShouldReturn404() throws Exception {
        long id = -1L;
        BDDMockito.doNothing().when(choiceRepository).deleteById(id);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/question/choice/{id}", HttpMethod.DELETE, professorHeader, String.class, id);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void createChoiceWhenTitleIsNullShouldReturn400() throws Exception {
        Choice choice = choiceRepository.findBy(1L);
        choice.setTitle(null);
        assertThat(createChoice(choice).getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void createChoiceWhenQuestionDoesNotExistsShouldReturn404() throws Exception {
        Choice choice = choiceRepository.findBy(1L);
        choice.setQuestion(new Question());
        assertThat(createChoice(choice).getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void createChoiceWhenEverythingIsRightShouldReturn200() throws Exception {
        Choice choice = choiceRepository.findBy(1L);
        choice.setId(Long.parseLong(null));
        assertThat(createChoice(choice).getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void createChoiceWithCorrectAnswerTrueShouldTriggerUpdateChoiceAndCreate() throws Exception {
        Choice choice = choiceRepository.findBy(2L);
        createChoice(choice);
        BDDMockito.verify(choiceRepository, Mockito.times(1)).save(choice);
        BDDMockito.verify(choiceRepository, Mockito.times(1)).updateAllOtherChoicesCorrectAnswerToFalse(choice, choice.getQuestion());
    }

    private ResponseEntity<String> createChoice(Choice choice) {
        BDDMockito.when(choiceRepository.save(choice)).thenReturn(choice);
        return testRestTemplate.exchange("/v1/professor/course/question/choice/", POST, new HttpEntity<>(choice, professorHeader.getHeaders()), String.class);
    }

}