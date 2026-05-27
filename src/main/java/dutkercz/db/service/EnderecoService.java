package dutkercz.db.service;

import dutkercz.db.domain.Endereco;
import dutkercz.db.dto.endereco.EnderecoResponseDto;
import dutkercz.db.dto.endereco.EnderecoUpdateDto;
import dutkercz.db.mapper.Mapper;
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
    private final Mapper mapper;

    @Transactional
    public EnderecoResponseDto updateEnderecoPorId(Long pessoaId,
                                                   Long enderecoId,
                                                   EnderecoUpdateDto enderecoRequestDto){
        Endereco endereco = enderecoRepository.findByIdAndPessoaId(enderecoId, pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Erro ao buscar endereço"));
        mapper.updateEnderecoFromDto(enderecoRequestDto, endereco);
        return mapper.toDto(endereco);
    }

    public Page<EnderecoResponseDto> listarPorPessoa(Long pessoaId, Pageable pageable) {
        pessoaService.buscarPorId(pessoaId);
        Page<Endereco> enderecos = enderecoRepository.findAllByPessoaId(pessoaId, pageable);
        return enderecos.map(mapper::toDto);
    }

    @Transactional
    public EnderecoResponseDto mudarEnderecoPrincipal(Long pessoaId, Long enderecoId) {

        Endereco novoPrincipal = enderecoRepository.findByIdAndPessoaId(enderecoId, pessoaId)
                   .orElseThrow(() -> new EntityNotFoundException("Erro ao buscar endereço"));

        enderecoRepository.resetarEnderecoPrincipal(pessoaId);

        novoPrincipal.setPrincipal(true);
        return mapper.toDto(novoPrincipal);
    }
}
