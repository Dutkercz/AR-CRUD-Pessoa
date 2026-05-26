package dutkercz.db.repository;

import dutkercz.db.domain.Endereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    Optional<Endereco> findByIdAndPessoaId(Long enderecoId, Long pessoaId);

    Page<Endereco> findAllByPessoaId(Long pessoaId, Pageable pageable);
}
