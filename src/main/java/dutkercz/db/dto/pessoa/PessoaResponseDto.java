package dutkercz.db.dto.pessoa;

import com.fasterxml.jackson.annotation.JsonFormat;
import dutkercz.db.dto.endereco.EnderecoResponseDto;

import java.time.LocalDate;
import java.util.List;

public record PessoaResponseDto(Long id,
                                String nome,
                                @JsonFormat(pattern = "dd/MM/yyyy")LocalDate dataNascimento,
                                String cpf,
                                int idade,
                                List<EnderecoResponseDto> enderecos
                               ) {
}
