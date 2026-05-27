package dutkercz.db.dto.pessoa;

import com.fasterxml.jackson.annotation.JsonFormat;
import dutkercz.db.dto.endereco.EnderecoRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
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
        @Schema(pattern = "dd/MM/yyyy", example = "10/08/1992")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,

        @Pattern(regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$", message = "O campo cpf está fora do formato esperado")
        String cpf,

        @Valid
        List<EnderecoRequestDto> enderecos
        ) {
}
