package dutkercz.db.service;

import dutkercz.db.domain.Pessoa;
import dutkercz.db.dto.pessoa.PessoaRequestDto;
import dutkercz.db.dto.pessoa.PessoaResponseDto;
import dutkercz.db.dto.pessoa.PessoaUpdateDto;
import dutkercz.db.mapper.EntitiesMapper;
import dutkercz.db.repository.PessoaRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

import static dutkercz.db.service.validations.ValidarEnderecoPrincipalUnico.validarEnderecoPrincipalUnico;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final EntitiesMapper entitiesMapper;

    @Transactional
    public PessoaResponseDto cadastrarPessoa(PessoaRequestDto requestDto){
        //no banco o cpf é salvo sem [.- ]
        String cpfLimpo = requestDto.cpf().replaceAll("[-. ]", "");
        if (pessoaRepository.existsByCpf(cpfLimpo)){
            throw new EntityExistsException("O cpf informado já possui cadastro");
        }
        validarEnderecoPrincipalUnico(requestDto.enderecos());
        Pessoa pessoa = pessoaRepository.save(entitiesMapper.toEntity(requestDto));
        return entitiesMapper.toDto(pessoa, calcularIdade(pessoa));
    }

    @Transactional(readOnly = true)
    public Page<PessoaResponseDto> listarPessoas(Pageable pageable) {
        return pessoaRepository.findAll(pageable)
                               .map(p -> entitiesMapper.toDto(p, calcularIdade(p)));
    }

    @Transactional
    public void deletarPessoa(Long id) {
        Pessoa pessoa = buscarPorId(id);
        pessoaRepository.delete(pessoa);
    }

    @Transactional
    public PessoaResponseDto atulizarNomePessoa(Long id, @Valid PessoaUpdateDto updateDto) {
        Pessoa pessoa = buscarPorId(id);
        entitiesMapper.updatePessoaFromDto(updateDto, pessoa);
        return entitiesMapper.toDto(pessoa, calcularIdade(pessoa));
    }

    private int calcularIdade(Pessoa pessoa) {
        LocalDate hoje = LocalDate.now();
        LocalDate nascimento = pessoa.getDataNascimento();
        return Period.between(nascimento, hoje).getYears();
    }

    public Pessoa buscarPorId(Long pessoaId) {
       return pessoaRepository.findById(pessoaId)
                  .orElseThrow(() ->
                                   new EntityNotFoundException("Pessoa com o id " + pessoaId + " não encontrada"));
    }
}
