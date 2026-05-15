package dutkercz.db.dto;

import java.time.LocalDate;

public record PessoaRequestDto(String nome,
                               LocalDate dataNascimento,
                               String cpf,
                               EnderecoRequestDto enderecoRequestDto
                               ) {
}
