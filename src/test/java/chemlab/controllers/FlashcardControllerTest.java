package chemlab.controllers;

import presentation.controllers.game.FlashcardController;
import chemlab.domain.model.game.Flashcard;
import shared.FlashcardDto;
import chemlab.domain.repository.game.FlashcardRepository;
import auth.config.CorsProperties;
import chemlab.service.game.FlashcardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
//@ExtendWith(SpringExtension.class)
@SpringBootTest
//@AutoConfigureMockMvc
//@WebMvcTest(FlashcardController.class)
class FlashcardControllerTest {
//    @Autowired
//    private WebApplicationContext webApplicationContext;

    @Autowired
    private FlashcardController flashcardController;
    @Autowired
    private FlashcardServiceImpl flashcardServiceImpl;
    // mocked in beforeeach
    private FlashcardRepository repoMock;
    @MockitoBean
    private CorsProperties corsProperties;

    @Captor
    ArgumentCaptor<Flashcard> valueCaptor;

    private final String question = "is this unique?";
    private final String answer = "yes";
    private final FlashcardDto flashcardDto = new FlashcardDto(question, answer);

    @BeforeEach
    void setUp() {
        repoMock = mock(FlashcardRepository.class);
        // manually inject flashcardService repo dependency -> repoMock
        ReflectionTestUtils.setField(flashcardServiceImpl, "flashcardRepo", repoMock);
    }

    @Test
    @DisplayName("It should instantiate the controller")
    void testInit() {
        assertNotNull(flashcardController);
    }

    @Test
    @DisplayName("It should not insert the flashcard into the db if it is not unique")
    void testCreateFail() {
        ArrayList<Flashcard> mockResult = new ArrayList<Flashcard>();
        mockResult.add(new Flashcard(question, answer));
        Mockito.doReturn(mockResult).when(repoMock).findAll();
        int beforeInsert = flashcardController.list().size();
        flashcardController.create(flashcardDto);
        assertEquals(beforeInsert, flashcardController.list().size());
    }

    @Test
    @DisplayName("It should insert the flashcard into the db")
    void testCreate() {
        String question1 = "Make me unique?";
        String answer1 = "yes";
        FlashcardDto flashcardDto2 = new FlashcardDto(question1, answer1);
        Flashcard fc = new Flashcard(question1, answer1);

        Mockito.doReturn(new ArrayList<Flashcard>()).when(repoMock).findAll();
        Mockito.doReturn(fc).when(repoMock).save(fc);

        flashcardController.create(flashcardDto2);

        verify(repoMock, times(1)).save(valueCaptor.capture());
    }

    @Test
    @DisplayName("It should request findByQuestion when queryQuestion called")
    void testQueryQuestion() {
        flashcardController.queryQuestions(question);
        verify(repoMock, times(1)).findByQuestion(question);
    }

    @Test
    @DisplayName("It should return a list of questions from the db")
    void testQueryAnswers() {
        flashcardController.queryAnswers(answer);
        verify(repoMock, times(1)).findByAnswer(answer);
    }
}
