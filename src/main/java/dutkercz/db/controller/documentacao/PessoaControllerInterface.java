package dutkercz.db.controller.documentacao;

import dutkercz.db.dto.pessoa.PessoaIdadeResponseDto;
import dutkercz.db.dto.pessoa.PessoaRequestDto;
import dutkercz.db.dto.pessoa.PessoaResponseDto;
import dutkercz.db.dto.pessoa.PessoaUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Tag(name = "Pessoas", description = "Endpoints para gerenciamento de pessoas")
public interface PessoaControllerInterface {

    @Operation(summary = "Cadastrar nova Pessoa", description = "Cadastra uma nova pessoa com endereços")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "201", description = "Sucesso ao cadastrar", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = PessoaResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Entradas inválidas"),
            @ApiResponse(responseCode = "404", description = "Entradas inválidas")
            })
    @PostMapping
    ResponseEntity<PessoaResponseDto> cadastrarPessoa(@RequestBody @Valid PessoaRequestDto requestDto,
                                                      UriComponentsBuilder builder);

    @Operation(summary = "Listar todos as pessoas",
            description = "Retorna uma lista completa de pessoas cadastradas e seus endereços")
    @ApiResponse(responseCode = "200", description = "Sucesso", content = @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = PessoaResponseDto.class))))
    @GetMapping
    ResponseEntity<Page<PessoaResponseDto>> listarPessoas(@ParameterObject Pageable pageable);

    @Operation(summary = "Atualizar Pessoa",
            description = "É possivel atualizar o nome de uma Pessoa cadastrada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso", content =
                { @Content(mediaType = "application/json", schema = @Schema(implementation = PessoaResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Erro atualizar"),
            @ApiResponse(responseCode = "400", description = "Entradas inválidas")})
    @PatchMapping("/{id}")
    ResponseEntity<PessoaResponseDto> atualizarPessoa(@PathVariable Long id,
                                                      @RequestBody @Valid PessoaUpdateDto updateDto);

    @Operation(summary = "Deletar Pessoa",
            description = "É possivel deletar uma Pessoa cadastrada juntamente com seus endereços")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sucesso"),
            @ApiResponse(responseCode = "404", description = "Erro deletar")})
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletarPessoa(@PathVariable Long id);

    @Operation(summary = "Verificar idade da Pessoa",
            description = "É possivel recuperar a idade de uma Pessoa com base na data de nascimento cadastrada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso", content =
                { @Content(mediaType = "application/json", schema = @Schema(implementation = PessoaResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Erro ao recuperar informações")})
    @GetMapping("/{id}/minha-idade")
    ResponseEntity<PessoaIdadeResponseDto> mostrarMinhaIdade(@PathVariable Long id);
}
