package hu.gyanikevin.szavazas.repository;

import hu.gyanikevin.szavazas.model.SzavazatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SzavazatRepository extends JpaRepository<SzavazatEntity, Long> {

    Optional<SzavazatEntity> findBySzavazas_IdAndKepviselo(String szavazasId, String kepviselo);
}
