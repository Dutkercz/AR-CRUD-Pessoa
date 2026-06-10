package dutkercz.db.repository;

import dutkercz.db.domain.Endereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    Optional<Endereco> findByIdAndPessoaId(Long enderecoId, Long pessoaId);

    Page<Endereco> findAllByPessoaId(Long pessoaId, Pageable pageable);

    // tirando o "clearAutomatically = true" da anotação, o hibernate consegue manter o contexto carregado
    @Modifying
    @Query("""
           UPDATE Endereco e
           SET e.principal = false
           WHERE e.pessoa.id = :pessoaId
           """)
    void resetarEnderecoPrincipal(@Param("pessoaId") Long pessoaId);

// havia feito essa Query de update, o problema é que ela não pode retornar a entidade, apenas boolean, int e alguma
// outra coisa, aí resolveu em parte, porque teria que refazer a consulta ao banco de qualquer maneira

//    @Modifying
//    @Query("""
//            UPDATE Endereco e
//            SET e.principal = true
//            WHERE e.pessoa.id = :pessoaId
//            AND e.id = :enderecoId
//            """)
//    void definirEnderecoPricipal(@Param("enderecoId") Long enderecoId, @Param("pessoaId") Long pessoaId);
}
