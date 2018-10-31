package br.com.devdojo.examgenerator.v1.course;

import br.com.devdojo.examgenerator.persistence.model.Course;
import br.com.devdojo.examgenerator.persistence.repository.CourseRepository;
import br.com.devdojo.examgenerator.persistence.repository.ProfessorRepository;
import br.com.devdojo.examgenerator.v1.ProfessorEndpointTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
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

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CourseEndpointTest {
    @MockBean
    private CourseRepository courseRepository;
    @MockBean
    private ProfessorRepository professorRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;
    private HttpEntity<Void> professorHeader;
    private HttpEntity<Void> wrongHeader;
    private Course course = mockCourse();

    private static Course mockCourse() {
        return Course.Builder.newCourse()
                .withId(1L)
                .withName("React")
                .withProfessor(ProfessorEndpointTest.mockProfessor())
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
        BDDMockito.when(courseRepository.findBy(course.getId())).thenReturn(course);
        BDDMockito.when(courseRepository.listCourses("")).thenReturn(Collections.singletonList(course));
        BDDMockito.when(courseRepository.listCourses("JAVA")).thenReturn(Collections.singletonList(course));
    }

    @Test
    public void getCourseByIdWhenTokenIsWrongShouldReturn403() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/1", HttpMethod.GET, wrongHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listCoursesWhenTokenIsWrongShouldReturn403() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/list?name=", HttpMethod.GET, wrongHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listAllCoursesWhenNameDoesNotExistsShouldReturnEmptyList() throws Exception {
        ResponseEntity<List<Course>> exchange = testRestTemplate.exchange("/v1/professor/course/list?name=xaxa", HttpMethod.GET, professorHeader, new ParameterizedTypeReference<List<Course>>() {
        });
        assertThat(exchange.getBody()).isEmpty();
    }

    @Test
    public void listAllCoursesWhenNameExistsShouldReturn200() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/list?name=xaxa", HttpMethod.GET, professorHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getCourseByIdWithouthIdShouldReturn400() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/", HttpMethod.GET, professorHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void getCourseByIdWhenCourseIdDoesNotExistsShouldReturn404() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/-1", HttpMethod.GET, professorHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void getCourseByIdWhenCourseExitsShouldReturn200() throws Exception {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/1", HttpMethod.GET, professorHeader, String.class);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void deleteCourseWhenIdExistsShouldReturn200() throws Exception {
        long id = 1L;
        BDDMockito.doNothing().when(courseRepository).delete(id);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/{id}", HttpMethod.GET, professorHeader, String.class, id);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void deleteCourseWhenIdDoesNotExistsShouldReturn404() throws Exception {
        long id = -1L;
        BDDMockito.doNothing().when(courseRepository).delete(id);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/professor/course/{id}", HttpMethod.GET, professorHeader, String.class, id);
        assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void createCourseWhenNameIsNullShouldReturn400() throws Exception {
        Course course = courseRepository.findBy(1L);
        course.setName(null);
        assertThat(createCourse(course).getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void createCourseWhenEverythingIsRightShouldReturn200() throws Exception {
        Course course = courseRepository.findBy(1L);
        course.setId(Long.parseLong(null));
        assertThat(createCourse(course).getStatusCodeValue()).isEqualTo(200);
    }

    private ResponseEntity<String> createCourse(Course course) {
        BDDMockito.when(courseRepository.save(course)).thenReturn(course);
        return testRestTemplate.exchange("/v1/professor/course/", POST, new HttpEntity<>(course, professorHeader.getHeaders()), String.class);
    }
}