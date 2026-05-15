package dutkercz.db.mapper;

import dutkercz.db.domain.Endereco;
import dutkercz.db.domain.Pessoa;
import dutkercz.db.dto.EnderecoRequestDto;
import dutkercz.db.dto.PessoaRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PessoaMapper {

    Pessoa toEntity(PessoaRequestDto requestDto);
    Endereco toEntity(EnderecoRequestDto requestDto);
}
