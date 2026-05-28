package dutkercz.db.controller.helper;

import dutkercz.db.domain.Pessoa;
import dutkercz.db.dto.endereco.EnderecoRequestDto;
import dutkercz.db.dto.pessoa.PessoaRequestDto;
import dutkercz.db.mapper.Mapper;
import dutkercz.db.mapper.MapperImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PessoaFactory {

    private static final Mapper mapper = new MapperImpl();

    public static PessoaRequestDto gerarDtoComEnderecoValida() {
        var endereco = new EnderecoRequestDto("rua", "123", "bairro", "cidade",
                                              "estado", "99999000", true);
        return new PessoaRequestDto("Cristian", LocalDate.of(1992, 8, 10),
                                     "12345678910", List.of(endereco));
    }

    public static PessoaRequestDto gerarDtoInvalidaComEndereco() {
        var endereco = new EnderecoRequestDto("rua", "123", "bairro", "cidade",
                                              "estado", "99999000", true);
        return new PessoaRequestDto("", LocalDate.of(1992, 8, 10),
                                    "12345678910", List.of(endereco));
    }

    public static PessoaRequestDto gerarDtoComDoisEnderecosInvalidos() {
        //vou usar o mesmo endereço 2x, o ponto aqui é verificar se (true) em "principal" gera o erro
        var endereco = new EnderecoRequestDto("rua", "123", "bairro", "cidade",
                                              "estado", "99999000", true);
        return new PessoaRequestDto("Cristian", LocalDate.of(1992, 8, 10),
                                    "12345678910", List.of(endereco, endereco));
    }
    
    public static List<Pessoa> gerarListaDePessoasValidas(int quantidade){
        List<Pessoa> pessoas = new ArrayList<>();
        EnderecoRequestDto endereco = new EnderecoRequestDto("rua", "123", "bairro", "cidade",
                                                                    "estado", "99999000", true);
        for (int i = 0; i < quantidade; i++) {
            PessoaRequestDto pessoaRequestDto =
                new PessoaRequestDto("Nome" + i, LocalDate.of(1992, 8, 10),
                                     "1234567891" + i, List.of(endereco));
            pessoas.add(mapper.toEntity(pessoaRequestDto));
        }
        return pessoas;
    }
}
