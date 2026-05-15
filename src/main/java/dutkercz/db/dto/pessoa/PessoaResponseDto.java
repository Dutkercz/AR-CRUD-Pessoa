package dutkercz.db.dto.pessoa;

import com.fasterxml.jackson.annotation.JsonFormat;
import dutkercz.db.dto.endereco.EnderecoRequestDto;

import java.time.LocalDate;

public record PessoaResponseDto(Long id,
                                String nome,
                                @JsonFormat(pattern = "dd/MM/yyyy")LocalDate dataNascimento,
                                String cpf,
                                EnderecoRequestDto enderecoRequestDto
                               ) {
}
