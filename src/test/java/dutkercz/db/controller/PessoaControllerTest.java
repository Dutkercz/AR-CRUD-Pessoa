package dutkercz.db.controller;

import dutkercz.db.controller.helper.PessoaFactory;
import dutkercz.db.dto.pessoa.PessoaRequestDto;
import dutkercz.db.dto.pessoa.PessoaUpdateDto;
import dutkercz.db.mapper.EntitiesMapper;
import dutkercz.db.repository.EnderecoRepository;
import dutkercz.db.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Autowired
    private EntitiesMapper entitiesMapper;

    @Autowired
    private JacksonTester<PessoaRequestDto> dtoRequestJson;

    @Autowired
    private JacksonTester<PessoaUpdateDto> dtoUpdateJson;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @BeforeEach
    void setUp() {
        pessoaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve retornar status 201 quando uma nova pessoa for cadastrada com sucesso")
    void shouldCreatePessoaSuccessfully() throws Exception {
        PessoaRequestDto requestDto = PessoaFactory.dtoValidoUmEnderecoValido();
        mockMvc.perform(
        post("/api/pessoas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dtoRequestJson.write(requestDto).getJson()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.nome").value(requestDto.nome()))
        .andExpect(jsonPath("$.cpf").value(requestDto.cpf()))
        .andExpect(jsonPath("$.enderecos").isArray());
    }

    @Test
    @DisplayName("Deve retornar status 400 quando forem passadas informações inválidas no cadastro de pessoas")
    void shouldNotCreatePessoaWhenHaveInvalidFields() throws Exception {
        PessoaRequestDto requestDto = PessoaFactory.dtoInvalidoUmEnderecoValido();

        mockMvc.perform(
        post("/api/pessoas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dtoRequestJson.write(requestDto).getJson()))
        .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @DisplayName("Deve retornar status 400 ao marcar dois endereços como principal no cadastro de pessoa")
    void shouldReturnStatusCode400() throws Exception {
        PessoaRequestDto requestDto = PessoaFactory.dtoValidoComDoisEnderecosInvalidos();

        mockMvc.perform(
        post("/api/pessoas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dtoRequestJson.write(requestDto).getJson()))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @DisplayName("Deve listar a pessoas cadastradas corretamente, junto com os endereços")
    void shouldListPessoasSuccessfully() throws Exception {
        var pessoas = pessoaRepository.saveAll(PessoaFactory.gerarListaDePessoasValidas(2));

        mockMvc.perform(get("/api/pessoas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
               //pessoa 1
                .andExpect(jsonPath("$.content[0].nome").value(pessoas.get(0).getNome()))
                .andExpect(jsonPath("$.content[0].cpf").value(pessoas.get(0).getCpf()))
                .andExpect(jsonPath("$.content[0].enderecos.length()").value(1))
               //pessoa 2
               .andExpect(jsonPath("$.content[1].nome").value(pessoas.get(1).getNome()))
               .andExpect(jsonPath("$.content[1].cpf").value(pessoas.get(1).getCpf()))
               .andExpect(jsonPath("$.content[1].enderecos.length()").value(1))
               .andDo(print());
    }

    @Test
    @DisplayName("Deve atualizar o nome de uma pessoa corretamente")
    void shouldUpdatePessoaSuccessfully() throws Exception {
        PessoaRequestDto requestDto = PessoaFactory.dtoValidoUmEnderecoValido();
        var pessoa = pessoaRepository.save(entitiesMapper.toEntity(requestDto));
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
        PessoaRequestDto requestDto = PessoaFactory.dtoValidoUmEnderecoValido();
        var pessoa = pessoaRepository.save(entitiesMapper.toEntity(requestDto));
        Long id = pessoa.getId();
        PessoaUpdateDto updateDto = new PessoaUpdateDto("");

        mockMvc.perform(patch("/api/pessoas/".concat(id.toString()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dtoUpdateJson.write(updateDto).getJson()))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @Test
    @DisplayName("Deve deletar uma Pessoa juntamente com os endereços corretamente")
    void shouldDeletePessoaSuccessfully() throws Exception {
        PessoaRequestDto requestDto = PessoaFactory.dtoValidoUmEnderecoValido();

        var pessoa = pessoaRepository.save(entitiesMapper.toEntity(requestDto));
        Long id = pessoa.getId();

        mockMvc.perform(delete("/api/pessoas/".concat(id.toString())))
               .andExpect(status().isNoContent())
               .andDo(print());

        //usei os asserts para verificar no banco, porque não há retorno nenhum, além do status code
        assertFalse(pessoaRepository.existsById(id), "O ID não deve existir");
        assertEquals(0, enderecoRepository.count(), "A contagem de endereços deve ser 0");
    }

    @Test
    @DisplayName("Deve retornar a idade corretamente")
    void shouldDisplayAgeSuccessfully() throws Exception {
        PessoaRequestDto requestDto = PessoaFactory.dtoValidoUmEnderecoValido();
        var pessoa = pessoaRepository.save(entitiesMapper.toEntity(requestDto));
        Long id = pessoa.getId();
        int idadeEsperada = Period.between(pessoa.getDataNascimento(), LocalDate.now()).getYears();

        mockMvc.perform(get("/api/pessoas/" + id + "/minha-idade"))
               .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Cristian"))
                .andExpect(jsonPath("$.idade").value(idadeEsperada))
               .andDo(print());
    }
}