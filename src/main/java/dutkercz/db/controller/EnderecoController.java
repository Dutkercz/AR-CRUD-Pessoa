package dutkercz.db.controller;

import dutkercz.db.dto.endereco.EnderecoResponseDto;
import dutkercz.db.dto.endereco.EnderecoUpdateDto;
import dutkercz.db.service.EnderecoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pessoas/{pessoaId}/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;

    @PatchMapping("/{enderecoId}")
    public ResponseEntity<EnderecoResponseDto> atualizarEndereco(@PathVariable Long pessoaId,
                                                                 @PathVariable Long enderecoId,
                                                                 @RequestBody EnderecoUpdateDto enderecoUpdateDto){
        return ResponseEntity.ok(enderecoService
                                         .updateEnderecoPorId(pessoaId, enderecoId, enderecoUpdateDto));
    }

    @PostMapping("/mudar-principal/{enderecoId}")
    public ResponseEntity<EnderecoResponseDto> mudarEnderecoPrincipal(@PathVariable Long pessoaId,
                                                                      @PathVariable Long enderecoId){
        return ResponseEntity.ok(enderecoService.mudarEnderecoPrincipal(pessoaId, enderecoId));
    }

    @GetMapping
    public ResponseEntity<Page<EnderecoResponseDto>> listarEnderecosDePessoa(@PathVariable Long pessoaId, Pageable pageable){
        return ResponseEntity.ok(enderecoService.listarPorPessoa(pessoaId, pageable));
    }
}
