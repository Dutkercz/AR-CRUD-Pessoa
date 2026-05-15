package dutkercz.db.dto.pessoa;

import com.fasterxml.jackson.annotation.JsonFormat;
import dutkercz.db.dto.endereco.EnderecoDto;

import java.time.LocalDate;
import java.util.List;

public record PessoaResponseDto(Long id,
                                String nome,
                                @JsonFormat(pattern = "dd/MM/yyyy")LocalDate dataNascimento,
                                String cpf,
                                List<EnderecoDto> enderecos
                               ) {
}
