package dutkercz.db.controller;

import dutkercz.db.dto.endereco.EnderecoRequestDto;
import dutkercz.db.dto.endereco.EnderecoResponseDto;
import dutkercz.db.dto.endereco.EnderecoUpdateDto;
import dutkercz.db.service.EnderecoService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pessoas/{pessoaId}/enderecos")
@RequiredArgsConstructor
public class EnderecoController implements dutkercz.db.controller.documentacao.EnderecoController {

    private final EnderecoService enderecoService;

    @Override
    public ResponseEntity<EnderecoResponseDto> adicionarEndereco(Long pessoaId, EnderecoRequestDto enderecoRequestDto) {
        return ResponseEntity.ok(enderecoService.adicionarEndereco(pessoaId, enderecoRequestDto));
    }

    @Override
    public ResponseEntity<EnderecoResponseDto> atualizarEndereco(@PathVariable Long pessoaId,
                                                                 @PathVariable Long enderecoId,
                                                                 @RequestBody EnderecoUpdateDto enderecoUpdateDto){
        return ResponseEntity.ok(enderecoService
                                         .updateEnderecoPorId(pessoaId, enderecoId, enderecoUpdateDto));
    }

    @Override
    public ResponseEntity<EnderecoResponseDto> mudarEnderecoPrincipal(@PathVariable Long pessoaId,
                                                                      @PathVariable Long enderecoId){
        return ResponseEntity.ok(enderecoService.mudarEnderecoPrincipal(pessoaId, enderecoId));
    }

    @Override
    public ResponseEntity<Page<EnderecoResponseDto>> listarEnderecosDePessoa(@PathVariable Long pessoaId,
                                                                             @ParameterObject Pageable pageable){
        return ResponseEntity.ok(enderecoService.listarPorPessoa(pessoaId, pageable));
    }
}
