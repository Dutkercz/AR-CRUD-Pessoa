package dutkercz.db.mapper;

import dutkercz.db.domain.Endereco;
import dutkercz.db.domain.Pessoa;
import dutkercz.db.dto.endereco.EnderecoDto;
import dutkercz.db.dto.pessoa.PessoaRequestDto;
import dutkercz.db.dto.pessoa.PessoaResponseDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PessoaMapper {

    Pessoa toEntity(PessoaRequestDto requestDto);

    @AfterMapping
    default void setPessoaToEndereco(@MappingTarget Pessoa pessoa){
        if (pessoa.getEnderecos() != null){
            for (Endereco endereco : pessoa.getEnderecos()) {
                endereco.setPessoa(pessoa);
            }
        }
    }

    PessoaResponseDto toDto(Pessoa pessoa);

    ///Utilitario para mapeamento da entidade de composição
    Endereco toEntity(EnderecoDto requestDto);
    EnderecoDto toDto(Endereco endereco);
}
