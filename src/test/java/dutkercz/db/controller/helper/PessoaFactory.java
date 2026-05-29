package dutkercz.db.controller.helper;

import dutkercz.db.domain.Pessoa;
import dutkercz.db.dto.endereco.EnderecoRequestDto;
import dutkercz.db.dto.pessoa.PessoaRequestDto;
import dutkercz.db.mapper.EntitiesMapper;
import dutkercz.db.mapper.EntitiesMapperImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PessoaFactory {

    private static final EntitiesMapper ENTITIES_MAPPER = new EntitiesMapperImpl();

    public static PessoaRequestDto dtoValidoUmEnderecoValido() {
        var endereco = new EnderecoRequestDto("rua", "123", "bairro", "cidade",
                                              "estado", "99999000", true);
        return new PessoaRequestDto("Cristian", LocalDate.of(1992, 8, 10),
                                     "12345678910", List.of(endereco));
    }

    public static PessoaRequestDto dtoInvalidoUmEnderecoValido() {
        var endereco = new EnderecoRequestDto("rua", "123", "bairro", "cidade",
                                              "estado", "99999000", true);
        return new PessoaRequestDto("", LocalDate.of(1992, 8, 10),
                                    "12345678910", List.of(endereco));
    }

    public static PessoaRequestDto dtoValidoComDoisEnderecosInvalidos() {
        //vou usar o mesmo endereço 2x, o ponto aqui é verificar se (true) em "principal" gera o erro
        var endereco = new EnderecoRequestDto("rua", "123", "bairro", "cidade",
                                              "estado", "99999000", true);
        return new PessoaRequestDto("Cristian", LocalDate.of(1992, 8, 10),
                                    "12345678910", List.of(endereco, endereco));
    }

    public static PessoaRequestDto dtoValidoComDoisEnderecosValidos() {
        var endereco = new EnderecoRequestDto("um", "123", "bairroUm", "cidadeUm",
                                              "estadoUm", "99999000", true);
        var endereco2 = new EnderecoRequestDto("rua", "123", "bairro", "cidade",
                                              "estado", "99999000", false);
        return new PessoaRequestDto("Cristian", LocalDate.of(1992, 8, 10),
                                    "12345678910", List.of(endereco, endereco2));
    }
    
    public static List<Pessoa> gerarListaDePessoasValidas(int quantidade){
        List<Pessoa> pessoas = new ArrayList<>();
        EnderecoRequestDto endereco = new EnderecoRequestDto("rua", "123", "bairro", "cidade",
                                                                    "estado", "99999000", true);
        for (int i = 0; i < quantidade; i++) {
            PessoaRequestDto pessoaRequestDto =
                new PessoaRequestDto("Nome" + i, LocalDate.of(1992, 8, 10),
                                     "1234567891" + i, List.of(endereco));
            pessoas.add(ENTITIES_MAPPER.toEntity(pessoaRequestDto));
        }
        return pessoas;
    }
}
