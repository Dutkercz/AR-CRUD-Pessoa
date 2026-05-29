package dutkercz.db.controller;

import dutkercz.db.controller.helper.PessoaFactory;
import dutkercz.db.dto.endereco.EnderecoRequestDto;
import dutkercz.db.dto.endereco.EnderecoUpdateDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
class EnderecoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private EntitiesMapper entitiesMapper;

    @Autowired
    private JacksonTester<EnderecoRequestDto> dtoRequestJson;

    @Autowired
    private JacksonTester<EnderecoUpdateDto> dtoUpdateJson;

    @BeforeEach
    void setUp() {
        pessoaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve adicionar um endereço marcado como principal com sucesso a uma Pessoa ja cadastrada")
    void shouldAddNewEnderecoAsPrincipal() throws Exception {
        var pessoaDtoValida = PessoaFactory.dtoValidoUmEnderecoValido();
        var pessoaId = pessoaRepository.save(entitiesMapper.toEntity(pessoaDtoValida)).getId();
        var enderecoRequestDto = new EnderecoRequestDto("rua", "123", "bairro", "cidade",
                                                                  "estado", "99999000", true);
        mockMvc.perform(post("/api/pessoas/"+pessoaId+"/enderecos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dtoRequestJson.write(enderecoRequestDto).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.principal").value(true))
               .andDo(print());
        assertEquals(2, enderecoRepository.count());
    }

    @Test
    @DisplayName("Deve adicionar com sucesso a uma Pessoa ja cadastrada")
    void shouldAddNewEndereco() throws Exception {
        var pessoaDtoValida = PessoaFactory.dtoValidoUmEnderecoValido();
        var pessoaId = pessoaRepository.save(entitiesMapper.toEntity(pessoaDtoValida)).getId();
        var enderecoRequestDto = new EnderecoRequestDto("rua", "123", "bairro", "cidade",
                                                        "estado", "99999000", false);
        mockMvc.perform(post("/api/pessoas/"+pessoaId+"/enderecos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dtoRequestJson.write(enderecoRequestDto).getJson()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.principal").value(false))
               .andDo(print());
        assertEquals(2, enderecoRepository.count());
    }

    @Test
    @DisplayName("Deve retornar status 400 quando houver campos inválidos")
    void shouldNotAddEnderecoWhenHaveInvalidFields() throws Exception {
        var pessoaDtoValida = PessoaFactory.dtoValidoUmEnderecoValido();
        var pessoaId = pessoaRepository.save(entitiesMapper.toEntity(pessoaDtoValida)).getId();
        var invalidRua = "";
        var enderecoRequestDto = new EnderecoRequestDto(invalidRua, "123", "bairro", "cidade",
                                                        "estado", "99999000", true);
        mockMvc.perform(post("/api/pessoas/"+pessoaId+"/enderecos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dtoRequestJson.write(enderecoRequestDto).getJson()))
               .andExpect(status().isBadRequest())
               .andDo(print());
        assertEquals(1, enderecoRepository.count());
    }

    @Test
    @DisplayName("Deve atualizar um endereco com sucesso, mesmo sem todos os campos preenchidos")
    void shouldUpdateEnderecoSuccessfully() throws Exception {
        var pessoaSalva = pessoaRepository.save(entitiesMapper.toEntity(
                PessoaFactory.dtoValidoUmEnderecoValido()));
        var enderecoSalvo = pessoaSalva.getEnderecos().getFirst();
        var enderecoUpdateDto = new EnderecoUpdateDto("ruaUpdate", "", null, "  ", "", "");

        mockMvc.perform(patch(
                "/api/pessoas/" + pessoaSalva.getId() + "/enderecos/" + enderecoSalvo.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dtoUpdateJson.write(enderecoUpdateDto).getJson()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(enderecoSalvo.getId()))
               .andExpect(jsonPath("$.rua").value(enderecoUpdateDto.rua()))
               .andExpect(jsonPath("$.numero").value(enderecoSalvo.getNumero()))
               .andExpect(jsonPath("$.bairro").value(enderecoSalvo.getBairro()))
               .andExpect(jsonPath("$.cidade").value(enderecoSalvo.getCidade()))
               .andDo(print());
    }

    @Test
    @DisplayName("Deve mudar o endereço principal com sucesso")
    void shouldUpdateEnderecoPrincipalSuccessfully() throws Exception {

        var pessoaSalva = pessoaRepository
                .save(entitiesMapper.toEntity(PessoaFactory.dtoValidoComDoisEnderecosValidos()));
        var novoPrincipal = pessoaSalva.getEnderecos().getFirst().isPrincipal() ?
                  pessoaSalva.getEnderecos().getLast() : pessoaSalva.getEnderecos().getFirst();

        mockMvc.perform(patch(
           "/api/pessoas/" + pessoaSalva.getId() + "/enderecos/mudar-principal/" + novoPrincipal.getId()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(novoPrincipal.getId()))
               .andExpect(jsonPath("$.rua").value(novoPrincipal.getRua()))
               .andExpect(jsonPath("$.principal").value(true))
               .andDo(print());

    }

    @Test
    @DisplayName("Deve listar os endereços de uma pessoa")
    void shouldListEnderecos() throws Exception {
        var pessoa = pessoaRepository
                .save(entitiesMapper.toEntity(PessoaFactory.dtoValidoUmEnderecoValido()));

        mockMvc.perform(get("/api/pessoas/" + pessoa.getId() + "/enderecos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id")
                                   .value(pessoa.getEnderecos().get(0).getId()))
                .andExpect(jsonPath("$.content.length()")
                                   .value(pessoa.getEnderecos().size()))
                 .andDo(print());
    }


}