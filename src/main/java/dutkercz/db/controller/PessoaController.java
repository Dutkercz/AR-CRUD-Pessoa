package dutkercz.db.controller;

import dutkercz.db.controller.documentacao.PessoaControllerInterface;
import dutkercz.db.dto.pessoa.PessoaRequestDto;
import dutkercz.db.dto.pessoa.PessoaResponseDto;
import dutkercz.db.dto.pessoa.PessoaUpdateDto;
import dutkercz.db.service.PessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/pessoas")
@RequiredArgsConstructor
public class PessoaController implements PessoaControllerInterface {

    private final PessoaService pessoaService;

    @Override
    public ResponseEntity<PessoaResponseDto> cadastrarPessoa(@RequestBody @Valid PessoaRequestDto requestDto,
                                                             UriComponentsBuilder builder){
        PessoaResponseDto responseDto = pessoaService.cadastrarPessoa(requestDto);
        URI uri = builder.path("/api/pessoas/{id}").buildAndExpand(responseDto.id()).toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @Override
    public ResponseEntity<Page<PessoaResponseDto>> listarPessoas(@ParameterObject Pageable pageable){
        return ResponseEntity.ok().body(pessoaService.listarPessoas(pageable));
    }

    @Override
    public ResponseEntity<PessoaResponseDto> atualizarPessoa(@PathVariable Long id,
                                                             @RequestBody @Valid PessoaUpdateDto updateDto){
        return ResponseEntity.ok(pessoaService.atulizarNomePessoa(id, updateDto));
    }

    @Override
    public ResponseEntity<Void> deletarPessoa(@PathVariable Long id){
        pessoaService.deletarPessoa(id);
        return ResponseEntity.noContent().build();
    }

//    @Override
//    public ResponseEntity<PessoaIdadeResponseDto> mostrarMinhaIdade(@PathVariable Long id){
//        return ResponseEntity.ok(pessoaService.mostrarMinhaIdade(id));
//    }
}
