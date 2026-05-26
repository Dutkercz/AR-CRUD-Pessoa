package dutkercz.db.dto.endereco;

public record EnderecoUpdateDto(String rua,
                                String numero,
                                String bairro,
                                String cidade,
                                String estado,
                                String cep) {
}
