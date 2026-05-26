package dutkercz.db.controller;

import dutkercz.db.dto.endereco.EnderecoResponseDto;
import dutkercz.db.dto.endereco.EnderecoUpdateDto;
import dutkercz.db.service.EnderecoService;
import lombok.RequiredArgsConstructor;
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
                                         .updateEnderecoById(pessoaId, enderecoId, enderecoUpdateDto));

    }
}
