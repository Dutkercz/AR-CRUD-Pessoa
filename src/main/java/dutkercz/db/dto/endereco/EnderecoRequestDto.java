package dutkercz.db.dto.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EnderecoRequestDto(
        @NotBlank(message = "O campo rua não pode estar em branco")
        String rua,
        @NotBlank(message = "O campo número não pode estar em branco")
        String numero,
        @NotBlank(message = "O campo bairro não pode estar em branco")
        String bairro,
        @NotBlank(message = "O campo cidade não pode estar em branco")
        String cidade,
        @NotBlank(message = "O campo estado não pode estar em branco")
        String estado,
        @NotBlank(message = "O campo cep não pode estar em branco")
        @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "O campo cep está fora do formato esperado")
        String cep) {
}
