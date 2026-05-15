package dutkercz.db.service;

import dutkercz.db.domain.Pessoa;
import dutkercz.db.dto.PessoaRequestDto;
import dutkercz.db.mapper.PessoaMapper;
import dutkercz.db.repository.PessoaRepository;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final PessoaMapper pessoaMapper;

    public PessoaService(PessoaRepository pessoaRepository, PessoaMapper pessoaMapper) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaMapper = pessoaMapper;
    }

    public void cadastrarPessoa(PessoaRequestDto requestDto){
        Pessoa pessoa = pessoaMapper.toEntity(requestDto);
    }
}
