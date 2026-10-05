package hu.gyanikevin.szavazas.service.impl;

import hu.gyanikevin.szavazas.dto.request.SzavazasRequestDto;
import hu.gyanikevin.szavazas.dto.request.SzavazatRequestDto;
import hu.gyanikevin.szavazas.dto.response.SzavazasResponseDto;
import hu.gyanikevin.szavazas.exception.SzavazasException;
import hu.gyanikevin.szavazas.model.SzavazasEntity;
import hu.gyanikevin.szavazas.model.SzavazatEntity;
import hu.gyanikevin.szavazas.repository.SzavazasRepository;
import hu.gyanikevin.szavazas.service.SzavazasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SzavazasServiceImpl implements SzavazasService {

    private final SzavazasRepository repository;

    @Override
    @Transactional
    public SzavazasResponseDto szavazasMentese(SzavazasRequestDto dto) {
        Instant idopont = dto.getIdopont().toInstant();
        if (idopont.getNano() != 0) {
            throw new SzavazasException(HttpStatus.BAD_REQUEST, "Az időpontot másodperc pontossággal kell megadni.");
        }

        Set<String> kepviselok = new HashSet<>();
        for (SzavazatRequestDto szavazat : dto.getSzavazatok()) {
            if (!kepviselok.add(szavazat.getKepviselo())) {
                throw new SzavazasException(HttpStatus.BAD_REQUEST, "Egy képviselő csak egyszer szavazhat: " + szavazat.getKepviselo());
            }
        }
        if (!kepviselok.contains(dto.getElnok())) {
            throw new SzavazasException(HttpStatus.BAD_REQUEST, "Az ülést vezető elnöknek is rendelkeznie kell leadott szavazattal.");
        }
        if (repository.existsByIdopont(idopont)) {
            throw new SzavazasException(HttpStatus.CONFLICT, "A megadott időpontra már létezik szavazás.");
        }

        SzavazasEntity entity = new SzavazasEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setIdopont(idopont);
        entity.setTargy(dto.getTargy());
        entity.setTipus(dto.getTipus());
        entity.setEljaras(dto.getEljaras());
        entity.setElnok(dto.getElnok());

        for (SzavazatRequestDto szavazatDto : dto.getSzavazatok()) {
            SzavazatEntity szavazat = new SzavazatEntity();
            szavazat.setSzavazas(entity);
            szavazat.setKepviselo(szavazatDto.getKepviselo());
            szavazat.setSzavazat(szavazatDto.getSzavazat());
            entity.getSzavazatok().add(szavazat);
        }

        repository.saveAndFlush(entity);
        return new SzavazasResponseDto(entity.getId());
    }
}
