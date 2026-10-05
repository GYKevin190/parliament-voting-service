package hu.gyanikevin.szavazas.repository;

import hu.gyanikevin.szavazas.model.SzavazasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface SzavazasRepository extends JpaRepository<SzavazasEntity, String> {

    boolean existsByIdopont(Instant idopont);
}
