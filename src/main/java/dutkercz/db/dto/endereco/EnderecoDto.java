package dutkercz.db.dto.endereco;

public record EnderecoDto(String rua,
                          String numero,
                          String bairro,
                          String cidade,
                          String estado,
                          String cep) {
}
