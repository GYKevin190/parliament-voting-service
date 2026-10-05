package hu.gyanikevin.szavazas.repository;

import hu.gyanikevin.szavazas.model.SzavazasEntity;
import hu.gyanikevin.szavazas.model.enums.EljarasTipus;
import hu.gyanikevin.szavazas.model.enums.SzavazasTipus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SzavazasRepository extends JpaRepository<SzavazasEntity, String> {

    boolean existsByIdopont(Instant idopont);

    Optional<SzavazasEntity> findFirstByTipusAndIdopontBeforeOrderByIdopontDesc(SzavazasTipus tipus, Instant idopont);

    @EntityGraph(attributePaths = "szavazatok")
    List<SzavazasEntity> findByIdopontGreaterThanEqualAndIdopontLessThanOrderByIdopontAsc(Instant kezdet, Instant veg);

    @EntityGraph(attributePaths = "szavazatok")
    List<SzavazasEntity> findByEljarasInAndIdopontGreaterThanEqualAndIdopontLessThanOrderByIdopontAsc(List<EljarasTipus> eljarasok, Instant kezdet, Instant veg);
}
