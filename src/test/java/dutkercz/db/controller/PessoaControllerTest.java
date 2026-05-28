package dutkercz.db.controller;

import dutkercz.db.dto.endereco.EnderecoRequestDto;
import dutkercz.db.dto.pessoa.PessoaRequestDto;
import dutkercz.db.mapper.Mapper;
import dutkercz.db.service.EnderecoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
class PessoaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnderecoService enderecoService;

    @Autowired
    private Mapper mapper;

    @Autowired
    private JacksonTester<PessoaRequestDto> dtoRequestJson;

    @Test
    @DisplayName("Deve retornar status 201 quando uma nova pessoa for cadastrada com sucesso")
    void shouldCreatePessoaSuccessfully() throws Exception {
        PessoaRequestDto requestDto =
                new PessoaRequestDto("Nome", LocalDate.of(1992, 8, 10),
                                     "123456789-10", List.of());

        var jsonReturn = mockMvc.perform(
                        post("/api/pessoas")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(dtoRequestJson.write(requestDto).getJson()))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.nome").value("Nome"))
                        .andExpect(jsonPath("$.cpf").value("12345678910"))
                        .andExpect(jsonPath("$.enderecos").isArray());
    }

    @Test
    @DisplayName("Deve retornar status 400 quando forem passadas informações inválidas no cadastro de pessoas")
    void shouldNotCreatePessoaWhenHaveInvalidFields() throws Exception {
        String invalidName = ""; // nome em branco é inválido
        PessoaRequestDto requestDto =
                new PessoaRequestDto(invalidName, LocalDate.of(1992, 8, 10),
                                     "123456789-10", List.of());

        var jsonReturn = mockMvc.perform(
                        post("/api/pessoas")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(dtoRequestJson.write(requestDto).getJson()))
                        .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @DisplayName("Deve retornar status 400 ao marcar dois endereços como principal")
    void shouldReturnStatusCode400() throws Exception {
        //vou usar o mesmo endereço, o ponto aqui é verificar se (true) em "principal" gera o erro
        var endereco = new EnderecoRequestDto("rua", "123", "bairro", "cidade",
                                              "estado", "99999-000", true);
        PessoaRequestDto requestDto =
                new PessoaRequestDto("Nome", LocalDate.of(1992, 8, 10),
                                     "123456789-10", List.of(endereco, endereco));

        mockMvc.perform(
        post("/api/pessoas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dtoRequestJson.write(requestDto).getJson()))
                .andExpect(status().isBadRequest()).andDo(print());
    }

}