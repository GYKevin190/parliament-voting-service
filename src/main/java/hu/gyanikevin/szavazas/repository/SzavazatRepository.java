package hu.gyanikevin.szavazas.repository;

import hu.gyanikevin.szavazas.model.SzavazatEntity;
import hu.gyanikevin.szavazas.model.enums.SzavazasTipus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface SzavazatRepository extends JpaRepository<SzavazatEntity, Long> {

    Optional<SzavazatEntity> findBySzavazas_IdAndKepviselo(String szavazasId, String kepviselo);

    @Query("select count(s) from SzavazatEntity s where s.szavazas.tipus <> :jelenletiTipus and s.szavazas.idopont >= :kezdet and s.szavazas.idopont < :veg")
    long countReszvetelek(@Param("jelenletiTipus") SzavazasTipus jelenletiTipus, @Param("kezdet") Instant kezdet, @Param("veg") Instant veg);
}
