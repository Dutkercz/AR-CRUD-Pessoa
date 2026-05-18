package dutkercz.db.dto.pessoa;

import com.fasterxml.jackson.annotation.JsonFormat;
import dutkercz.db.dto.endereco.EnderecoDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

/// O cpf não verifica se é valido, apenas se está no formato de 11 digitos.
public record PessoaRequestDto(
        @NotBlank(message = "O campo nome não pode estar em branco")
        @Size(min = 4, message = "O campo nome deve ter no mínimo 4 caracteres")
        @Pattern(regexp = "^[A-Za-z ]+$")
        String nome,

        @NotNull
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,

        @Pattern(regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$", message = "O campo cpf está fora do formato esperado")
        String cpf,
        List<EnderecoDto> enderecos
        ) {
}
