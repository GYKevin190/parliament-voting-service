package hu.gyanikevin.szavazas.repository;

import hu.gyanikevin.szavazas.model.SzavazasEntity;
import hu.gyanikevin.szavazas.model.enums.SzavazasTipus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface SzavazasRepository extends JpaRepository<SzavazasEntity, String> {

    boolean existsByIdopont(Instant idopont);

    Optional<SzavazasEntity> findFirstByTipusAndIdopontBeforeOrderByIdopontDesc(SzavazasTipus tipus, Instant idopont);
}
