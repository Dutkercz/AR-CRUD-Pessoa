package dutkercz.db.service;

import dutkercz.db.domain.Endereco;
import dutkercz.db.domain.Pessoa;
import dutkercz.db.dto.pessoa.PessoaRequestDto;
import dutkercz.db.dto.pessoa.PessoaResponseDto;
import dutkercz.db.dto.pessoa.PessoaUpdateDto;
import dutkercz.db.mapper.Mapper;
import dutkercz.db.mapper.MapperImpl;
import dutkercz.db.repository.PessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @Spy
    private Mapper mapper = new MapperImpl();

    @InjectMocks
    private PessoaService pessoaService;

    private Pessoa pessoa1;
    private Pessoa pessoa2;
    private PessoaRequestDto requestDto1;

    @BeforeEach
    void setUp() {
        requestDto1 = new PessoaRequestDto("Pessoa Um", LocalDate.of(2000, 1, 1),
                                           "12345678910", new ArrayList<>());
        pessoa1 = new Pessoa(1L, "Pessoa Um", LocalDate.of(2000, 1, 1),
                             "12345678910", new ArrayList<>());
        Endereco endereco = new Endereco(1L, "rua1", "1", "bairroUm",
                                         "cidadeUm", "estadoUm", "12345000",true, pessoa1);
        pessoa1.addEndereco(endereco);

        pessoa2 = new Pessoa(2L, "Pessoa Dois", LocalDate.of(2000, 2, 2),
                             "12345678911", new ArrayList<>());

        Endereco endereco2 = new Endereco(2L, "rua2", "2", "bairroDois",
                             "cidadeDois", "estadoDois", "12345001", true, pessoa2);
        pessoa2.addEndereco(endereco2);
    }

    @Test
    @DisplayName("Deve retornar uma lista com todas as Pessoas cadastradas e seu endereços")
    void shouldFindAllPessoasSuccessfully() {
        Page<Pessoa> pessoasPage = new PageImpl<>(List.of(pessoa1, pessoa2) );
        when(pessoaRepository.findAll(any(Pageable.class))).thenReturn(pessoasPage);
        Page<PessoaResponseDto> result = pessoaService.listarPessoas(Pageable.ofSize(1));

        assertNotNull(result.getContent());
        assertEquals(pessoasPage.getTotalElements(), result.getTotalElements());
        assertNotNull(result.getContent().getFirst().enderecos());
        assertEquals(pessoasPage.getContent().getFirst().getId(), result.getContent().getFirst().id());
        verify(pessoaRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não houver nenhum elemento")
    void shouldNotFindAnyPessoas() {
        when(pessoaRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        Page<PessoaResponseDto> result = pessoaService.listarPessoas(Pageable.ofSize(1));

        assertNotNull(result.getContent());
        assertEquals(0, result.getTotalElements());
        verify(pessoaRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Deve criar um novo elemento da entidade Pessoa")
    void shouldCratePessoaSuccessfully() {
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa1);

        PessoaResponseDto result = pessoaService.cadastrarPessoa(requestDto1);

        assertNotNull(result);
        assertEquals(pessoa1.getNome(), result.nome());
        assertEquals(1, result.enderecos().size());
        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
    }

    @Test
    @DisplayName("Deve deletar um elemento da entidade Pessoa através do ID")
    void shouldDeletePessoaSuccessfully() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa1));

        assertDoesNotThrow(() -> pessoaService.deletarPessoa(1L));
        verify(pessoaRepository, times(1)).findById(1L);
        verify(pessoaRepository, times(1)).delete(pessoa1);
    }

    @Test
    @DisplayName("Deve lançar uma EntityNotFoundExcept. quando receber não houver uma correspondencia do ID")
    void shouldNotDeletePessoaSuccessfully() {
        when(pessoaRepository.findById(99L)).thenReturn(Optional.empty());

        var result = assertThrows(EntityNotFoundException.class,
                                  () -> pessoaService.deletarPessoa(99L));
        assertEquals("Pessoa com o id 99 não encontrada", result.getMessage());
        verify(pessoaRepository, times(1)).findById(99L);
        verify(pessoaRepository, never()).delete(any(Pessoa.class));
    }

    @Test
    @DisplayName("Deve atualizar um nome de Pessoa corretamente")
    void shouldUpdatePessoaSuccessfully() {
        PessoaUpdateDto updateDto = new PessoaUpdateDto("Pessoa Um Atulizada");
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa1));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa1);

        PessoaResponseDto result = pessoaService.atulizarNomePessoa(1L, updateDto);
        assertNotNull(result);
        assertNotNull(result.enderecos());
        assertEquals(updateDto.nome(), result.nome());
        verify(pessoaRepository, times(1)).findById(1L);
        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
    }
}
