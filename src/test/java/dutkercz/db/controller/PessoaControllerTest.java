package dutkercz.db.controller;

import dutkercz.db.dto.endereco.EnderecoRequestDto;
import dutkercz.db.dto.pessoa.PessoaRequestDto;
import dutkercz.db.dto.pessoa.PessoaUpdateDto;
import dutkercz.db.mapper.Mapper;
import dutkercz.db.repository.PessoaRepository;
import dutkercz.db.service.EnderecoService;
import org.junit.jupiter.api.AfterEach;
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
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private JacksonTester<PessoaUpdateDto> dtoUpdateJson;

    @Autowired
    private PessoaRepository pessoaRepository;

    @AfterEach
    void setUp() {
        pessoaRepository.deleteAll();
    }

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
    @DisplayName("Deve retornar status 400 ao marcar dois endereços como principal no cadastro de pessoa")
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

    @Test
    @DisplayName("Deve listar a pessoas cadastradas corretamente, junto com os endereços")
    void shouldListPessoasSuccessfully() throws Exception {
        var endereco = new EnderecoRequestDto("rua", "123", "bairro", "cidade",
                                              "estado", "99999-000", true);
        PessoaRequestDto pessoaRequestDto1 =
                new PessoaRequestDto("Cristian", LocalDate.of(1992, 8, 10),
                                     "123456789-10", List.of(endereco));
        PessoaRequestDto pessoaRequestDto2 =
                new PessoaRequestDto("Tiago", LocalDate.of(1992, 8, 10),
                                     "123456789-11", List.of(endereco));
        pessoaRepository.saveAll(Stream.of(pessoaRequestDto1, pessoaRequestDto2)
                                    .map(mapper::toEntity).toList());

        mockMvc.perform(get("/api/pessoas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
               //pessoa 1
                .andExpect(jsonPath("$.content[0].nome").value("Cristian"))
                .andExpect(jsonPath("$.content[0].cpf").value("12345678910"))
                .andExpect(jsonPath("$.content[0].enderecos.length()").value(1))
               //pessoa 2
               .andExpect(jsonPath("$.content[1].nome").value("Tiago"))
               .andExpect(jsonPath("$.content[1].cpf").value("12345678911"))
               .andExpect(jsonPath("$.content[1].enderecos.length()").value(1));
    }

    @Test
    @DisplayName("Deve atualizar o nome de uma pessoa corretamente")
    void shouldUpdatePessoaSuccessfully() throws Exception {
        var endereco = new EnderecoRequestDto("rua", "123", "bairro", "cidade",
                                              "estado", "99999-000", true);
        PessoaRequestDto requestDto =
                new PessoaRequestDto("Cristian", LocalDate.of(1992, 8, 10),
                                     "123456789-10", List.of(endereco));

        var pessoa = pessoaRepository.save( mapper.toEntity(requestDto));
        Long id = pessoa.getId();
        PessoaUpdateDto updateDto = new PessoaUpdateDto("Nome atualizado");

        mockMvc.perform(patch("/api/pessoas/".concat(id.toString()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dtoUpdateJson.write(updateDto).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome atualizado"))
               .andDo(print());
    }

    @Test
    @DisplayName("Deve receber status 400 ao atualizar o nome de uma pessoa incorretamente")
    void shouldNotUpdatePessoaWhenNameIsInvalid() throws Exception {
        var endereco = new EnderecoRequestDto("rua", "123", "bairro", "cidade",
                                              "estado", "99999-000", true);
        PessoaRequestDto pessoaRequestDto1 =
                new PessoaRequestDto("Cristian", LocalDate.of(1992, 8, 10),
                                     "123456789-10", List.of(endereco));

        var pessoa = pessoaRepository.save(mapper.toEntity(pessoaRequestDto1));
        Long id = pessoa.getId();
        PessoaUpdateDto updateDto = new PessoaUpdateDto("");

        mockMvc.perform(patch("/api/pessoas/".concat(id.toString()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dtoUpdateJson.write(updateDto).getJson()))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

}