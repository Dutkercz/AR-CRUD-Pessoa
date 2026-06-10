package dutkercz.db.service;

import dutkercz.db.domain.Endereco;
import dutkercz.db.domain.Pessoa;
import dutkercz.db.dto.endereco.EnderecoRequestDto;
import dutkercz.db.dto.endereco.EnderecoResponseDto;
import dutkercz.db.dto.endereco.EnderecoUpdateDto;
import dutkercz.db.mapper.EntitiesMapper;
import dutkercz.db.repository.EnderecoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final PessoaService pessoaService;
    private final EntitiesMapper entitiesMapper;

    @Transactional
    public EnderecoResponseDto updateEnderecoPorId(Long pessoaId,
                                                   Long enderecoId,
                                                   EnderecoUpdateDto enderecoRequestDto){
        Endereco endereco = enderecoRepository.findByIdAndPessoaId(enderecoId, pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Erro ao buscar endereço"));
        entitiesMapper.updateEnderecoFromDto(enderecoRequestDto, endereco);
        return entitiesMapper.toDto(endereco);
    }

    public Page<EnderecoResponseDto> listarPorPessoa(Long pessoaId, Pageable pageable) {
        pessoaService.buscarPorId(pessoaId);
        Page<Endereco> enderecos = enderecoRepository.findAllByPessoaId(pessoaId, pageable);
        return enderecos.map(entitiesMapper::toDto);
    }

    @Transactional
    public EnderecoResponseDto mudarEnderecoPrincipal(Long pessoaId, Long enderecoId) {
        Endereco novoPrincipal = enderecoRepository.findByIdAndPessoaId(enderecoId, pessoaId)
                   .orElseThrow(() -> new EntityNotFoundException("Erro ao buscar endereço"));

        enderecoRepository.resetarEnderecoPrincipal(pessoaId);
        novoPrincipal.setPrincipal(true);
        return entitiesMapper.toDto(novoPrincipal);
    }

    @Transactional
    public EnderecoResponseDto adicionarEndereco(Long pessoaId, EnderecoRequestDto enderecoRequestDto) {
        Pessoa pessoa = pessoaService.buscarPorId(pessoaId);
        if (enderecoRequestDto.principal()){
            enderecoRepository.resetarEnderecoPrincipal(pessoaId);
        }
        Endereco endereco = entitiesMapper.toEntity(enderecoRequestDto);
        endereco.setPessoa(pessoa);
        return  entitiesMapper.toDto(enderecoRepository.save(endereco));
    }
}
