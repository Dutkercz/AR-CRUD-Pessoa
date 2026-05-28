package dutkercz.db.controller.documentacao;

import dutkercz.db.dto.endereco.EnderecoRequestDto;
import dutkercz.db.dto.endereco.EnderecoResponseDto;
import dutkercz.db.dto.endereco.EnderecoUpdateDto;
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

@Tag(name = "Endereços", description = "Endpoints para gerenciamento de endereços das pessoas")
public interface EnderecoController {

    @Operation(summary = "Adicionar endereço",
            description = "Adiciona um endereço à lista de endereços de uma pessoa")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "201", description = "Sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoResponseDto.class))}),
             @ApiResponse(responseCode = "404", description = "Entradas inválidas", content = @Content)})
    @PostMapping
    ResponseEntity<EnderecoResponseDto> adicionarEndereco(@PathVariable Long pessoaId,
                                                          @RequestBody @Valid EnderecoRequestDto enderecoRequestDto);


    @Operation(summary = "Atualizar endereço",
            description = "Atualiza um endereço recebendo um ID como referencia")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoResponseDto.class))}),
             @ApiResponse(responseCode = "404", description = "Entradas inválidas", content = @Content)})
    @PatchMapping("/{enderecoId}")
    ResponseEntity<EnderecoResponseDto> atualizarEndereco(@PathVariable Long pessoaId, @PathVariable Long enderecoId,
                                                          @RequestBody EnderecoUpdateDto enderecoUpdateDto);

    @Operation(summary = "Mudar endereço principal",
            description = "Define um endereço como sendo o principal")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoResponseDto.class))}),
             @ApiResponse(responseCode = "404", description = "Entradas inválidas", content = @Content)})
    @PatchMapping("/mudar-principal/{enderecoId}")
    ResponseEntity<EnderecoResponseDto> mudarEnderecoPrincipal(@PathVariable Long pessoaId,
                                                               @PathVariable Long enderecoId);


    @Operation(summary = "Listar endereços",
            description = "Lista todos os endereços de uma pessoa")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Sucesso", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EnderecoResponseDto.class)))}),
             @ApiResponse(responseCode = "404", description = "Entradas inválidas", content = @Content)})
    @GetMapping
    ResponseEntity<Page<EnderecoResponseDto>> listarEnderecosDePessoa(@PathVariable Long pessoaId,
                                                                      @ParameterObject Pageable pageable);
}
