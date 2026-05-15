package dutkercz.db.mapper;

import dutkercz.db.domain.Endereco;
import dutkercz.db.domain.Pessoa;
import dutkercz.db.dto.endereco.EnderecoDto;
import dutkercz.db.dto.pessoa.PessoaRequestDto;
import dutkercz.db.dto.pessoa.PessoaResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PessoaMapper {

    Pessoa toEntity(PessoaRequestDto requestDto);
    PessoaResponseDto toDto(Pessoa pessoa);

    ///Utilitario para mapeamento da entidade de composição
    Endereco toEntity(EnderecoDto requestDto);
    EnderecoDto toDto(Endereco endereco);
}
