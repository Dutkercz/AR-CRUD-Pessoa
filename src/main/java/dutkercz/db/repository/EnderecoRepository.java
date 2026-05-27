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

    @Modifying(clearAutomatically = true)
    @Query("""
           UPDATE Endereco e
           SET e.principal = false
           WHERE e.pessoa.id = :pessoaId
           """)
    void resetarEnderecoPrincipal(@Param("pessoaId") Long pessoaId);
}
