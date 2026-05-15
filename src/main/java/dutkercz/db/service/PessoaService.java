package dutkercz.db.service;

import dutkercz.db.domain.Pessoa;
import dutkercz.db.dto.pessoa.PessoaRequestDto;
import dutkercz.db.dto.pessoa.PessoaResponseDto;
import dutkercz.db.mapper.PessoaMapper;
import dutkercz.db.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
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
}
