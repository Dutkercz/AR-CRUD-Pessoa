package dutkercz.db.service;

import dutkercz.db.domain.Pessoa;
import dutkercz.db.dto.pessoa.PessoaRequestDto;
import dutkercz.db.dto.pessoa.PessoaResponseDto;
import dutkercz.db.dto.pessoa.PessoaUpdateDto;
import dutkercz.db.mapper.PessoaMapper;
import dutkercz.db.repository.PessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final PessoaMapper pessoaMapper;

    @Transactional
    public PessoaResponseDto cadastrarPessoa(PessoaRequestDto requestDto){
        Pessoa pessoa = pessoaRepository.save(pessoaMapper.toEntity(requestDto));
        return pessoaMapper.toDto(pessoa);
    }

    @Transactional(readOnly = true)
    public Page<PessoaResponseDto> listarPessoas(Pageable pageable) {
        return pessoaRepository.findAll(pageable).map(pessoaMapper::toDto);
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
        pessoa = pessoaMapper.updatePessoaFromDto(updateDto, pessoa);
        pessoaRepository.save(pessoa);
        return pessoaMapper.toDto(pessoa);
    }
}
