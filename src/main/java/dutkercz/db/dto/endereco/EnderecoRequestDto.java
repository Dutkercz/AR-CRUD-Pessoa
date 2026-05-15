package dutkercz.db.dto.endereco;

public record EnderecoRequestDto(String rua,
                                 String numero,
                                 String bairro,
                                 String cidade,
                                 String estado,
                                 String cep) {
}
