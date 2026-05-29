package dutkercz.db.mapper;

import dutkercz.db.domain.Endereco;
import dutkercz.db.domain.Pessoa;
import dutkercz.db.dto.endereco.EnderecoRequestDto;
import dutkercz.db.dto.endereco.EnderecoResponseDto;
import dutkercz.db.dto.endereco.EnderecoUpdateDto;
import dutkercz.db.dto.pessoa.PessoaRequestDto;
import dutkercz.db.dto.pessoa.PessoaResponseDto;
import dutkercz.db.dto.pessoa.PessoaUpdateDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EntitiesMapper {

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
    EnderecoResponseDto toDto(Endereco endereco);
    Endereco updateEnderecoFromDto(EnderecoUpdateDto enderecoUpdateDto, @MappingTarget Endereco endereco);

    ///utilitario que evitar sobreescrever com strings vazias e nullas quem venham de dtos de update
    @Condition
    default boolean verificaNullos(String value){
        return value != null && !value.isEmpty();
    }

}
