package dutkercz.db.service;

import dutkercz.db.domain.Pessoa;
import dutkercz.db.dto.pessoa.PessoaIdadeResponseDto;
import dutkercz.db.dto.pessoa.PessoaRequestDto;
import dutkercz.db.dto.pessoa.PessoaResponseDto;
import dutkercz.db.dto.pessoa.PessoaUpdateDto;
import dutkercz.db.mapper.Mapper;
import dutkercz.db.repository.PessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final Mapper mapper;

    @Transactional
    public PessoaResponseDto cadastrarPessoa(PessoaRequestDto requestDto){
        Pessoa pessoa = pessoaRepository.save(mapper.toEntity(requestDto));
        return mapper.toDto(pessoa);
    }

    @Transactional(readOnly = true)
    public Page<PessoaResponseDto> listarPessoas(Pageable pageable) {
        return pessoaRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional
    public void deletarPessoa(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
            .orElseThrow(() ->
                                 new EntityNotFoundException("Pessoa com o id " + id + " não encontrada"));
        pessoaRepository.delete(pessoa);
    }

    @Transactional
    public PessoaResponseDto atulizarNomePessoa(Long id, @Valid PessoaUpdateDto updateDto) {
        Pessoa pessoa =pessoaRepository.findById(id)
            .orElseThrow(() ->
                                 new EntityNotFoundException("Pessoa com o id " + id + " não encontrada"));
        pessoa = mapper.updatePessoaFromDto(updateDto, pessoa);
        pessoaRepository.save(pessoa);
        return mapper.toDto(pessoa);
    }

    public PessoaIdadeResponseDto mostrarMinhaIdade(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
                   .orElseThrow(() ->
                                        new EntityNotFoundException("Pessoa com o id " + id + " não encontrada"));
        LocalDate hoje = LocalDate.now();
        LocalDate nascimento = pessoa.getDataNascimento();
        int idade = Period.between(nascimento, hoje).getYears();
        return new PessoaIdadeResponseDto(pessoa.getNome(), idade);
    }
}
