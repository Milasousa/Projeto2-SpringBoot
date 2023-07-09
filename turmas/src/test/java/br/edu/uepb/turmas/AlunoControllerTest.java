package br.edu.uepb.turmas;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import br.edu.uepb.turmas.controller.AlunoController;
import br.edu.uepb.turmas.domain.Aluno;
import br.edu.uepb.turmas.mapper.AlunoMapper;
import br.edu.uepb.turmas.services.AlunoService;
import javassist.NotFoundException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@EnableAutoConfiguration
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AlunoControllerTest {

    private static final String ALUNO_API_URL_PATH = "/alunos";
    private static final Long VALID_ALUNO_ID = 1L;
    private static final Long INVALID_ALUNO_ID = 10L;

    private MockMvc mockMvc;
    @Mock
    private AlunoService alunoService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AlunoMapper alunoMapper;

    @InjectMocks
    private AlunoController alunoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(alunoController).build();
    }

    @Nested
    class GetMethodsTest {
        private Aluno aluno;

        @BeforeEach
        void setUp() {
            long id = 1;
            aluno = Aluno.AlunoBuilder()
                    .id(id)
                    .nome("Andrea")
                    .email("c@gmail.com")
                    .username("andrea")
                    .password("123")
                    .build();
        }

        @Test
        void whenGETIsCalledThenOkStatusIsReturned() throws Exception {
            when(alunoService.listAllAlunos()).thenReturn(List.of(aluno));

            mockMvc.perform(MockMvcRequestBuilders.get("/alunos")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.nome", is(aluno.getNome())));

        }

        @Test
        void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
            when(alunoService.findById(VALID_ALUNO_ID)).thenReturn(aluno);

            mockMvc.perform(MockMvcRequestBuilders.get(ALUNO_API_URL_PATH + "/" + VALID_ALUNO_ID)
             .contentType(MediaType.APPLICATION_JSON)
             .characterEncoding("UTF-8"))
             .andDo(MockMvcResultHandlers.print())
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.nome", is(aluno.getNome())));
        }

        @Test
        void whenGETIsCalledWithInvalidIdThenBadRequestStatusIsReturned() throws Exception {
            when(alunoService.findById(INVALID_ALUNO_ID)).thenThrow(NotFoundException.class);

            mockMvc.perform(MockMvcRequestBuilders.get(ALUNO_API_URL_PATH + "/" + INVALID_ALUNO_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest());
    }
    }

    @Nested
    class DeleteMethodsTest {
        @Test
        void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
            doNothing().when(alunoService).apagarAlunos(VALID_ALUNO_ID);

            mockMvc.perform(MockMvcRequestBuilders.delete(ALUNO_API_URL_PATH + "/" + VALID_ALUNO_ID)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }
    }
}