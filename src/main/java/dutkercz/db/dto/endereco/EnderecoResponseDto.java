package dutkercz.db.dto.endereco;

public record EnderecoResponseDto(
        Long id,
        String rua,
        String numero,
        String bairro,
        String cidade,
        String estado,
        String cep) {
}
