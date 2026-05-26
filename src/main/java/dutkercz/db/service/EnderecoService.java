package dutkercz.db.service;

import dutkercz.db.domain.Endereco;
import dutkercz.db.dto.endereco.EnderecoResponseDto;
import dutkercz.db.dto.endereco.EnderecoUpdateDto;
import dutkercz.db.mapper.Mapper;
import dutkercz.db.repository.EnderecoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final Mapper mapper;

    public EnderecoResponseDto updateEnderecoById(Long pessoaId,
                                                  Long enderecoId,
                                                  EnderecoUpdateDto enderecoRequestDto){
        Endereco endereco = enderecoRepository.findByIdAndPessoaId(enderecoId, pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Erro ao buscar endereço"));
        endereco = enderecoRepository.save(mapper.updateEnderecoFromDto(enderecoRequestDto, endereco));
        return mapper.toDto(endereco);
    }
}
