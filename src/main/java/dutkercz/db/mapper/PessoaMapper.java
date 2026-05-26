package dutkercz.db.mapper;

import dutkercz.db.domain.Endereco;
import dutkercz.db.domain.Pessoa;
import dutkercz.db.dto.endereco.EnderecoRequestDto;
import dutkercz.db.dto.pessoa.PessoaRequestDto;
import dutkercz.db.dto.pessoa.PessoaResponseDto;
import dutkercz.db.dto.pessoa.PessoaUpdateDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PessoaMapper {

    @Mapping(target = "cpf", expression = "java(requestDto.cpf().replaceAll(\"[.-]\", \"\"))")
    Pessoa toEntity(PessoaRequestDto requestDto);

    @AfterMapping
    default void setPessoaToEndereco(@MappingTarget Pessoa pessoa){
        if (pessoa.getEnderecos() != null){
            for (Endereco endereco : pessoa.getEnderecos()) {
                endereco.setPessoa(pessoa);
            }
        }
    }

    Pessoa updatePessoaFromDto(PessoaUpdateDto updateDto, @MappingTarget Pessoa pessoa);

    PessoaResponseDto toDto(Pessoa pessoa);

    ///Utilitario para mapeamento da entidade de composição
    Endereco toEntity(EnderecoRequestDto requestDto);
    EnderecoRequestDto toDto(Endereco endereco);
}
