package dutkercz.db.service.validations;

import dutkercz.db.dto.endereco.EnderecoRequestDto;
import dutkercz.db.exception.custom.BusinessException;

import java.util.List;

public class ValidarEnderecoPrincipalUnico {

    public static void validarEnderecoPrincipalUnico(List<EnderecoRequestDto> enderecos) {
        var total = enderecos.stream().filter(EnderecoRequestDto::principal).count();
        if (total>1){
            throw new BusinessException("Apenas um endereco principal é permitido");
        }
    }
}
