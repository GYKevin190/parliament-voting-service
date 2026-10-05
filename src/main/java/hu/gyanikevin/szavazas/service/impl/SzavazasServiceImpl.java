package hu.gyanikevin.szavazas.service.impl;

import hu.gyanikevin.szavazas.dto.request.SzavazasRequestDto;
import hu.gyanikevin.szavazas.dto.request.SzavazatRequestDto;
import hu.gyanikevin.szavazas.dto.response.SzavazasResponseDto;
import hu.gyanikevin.szavazas.dto.response.SzavazasEredmenyResponseDto;
import hu.gyanikevin.szavazas.dto.response.SzavazatResponseDto;
import hu.gyanikevin.szavazas.exception.SzavazasException;
import hu.gyanikevin.szavazas.model.SzavazasEntity;
import hu.gyanikevin.szavazas.model.SzavazatEntity;
import hu.gyanikevin.szavazas.model.enums.SzavazasTipus;
import hu.gyanikevin.szavazas.repository.SzavazasRepository;
import hu.gyanikevin.szavazas.repository.SzavazatRepository;
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

    private static final int OSSZES_KEPVISELO_SZAMA = 200;

    private final SzavazasRepository repository;
    private final SzavazatRepository szavazatRepository;

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

    @Override
    @Transactional(readOnly = true)
    public SzavazatResponseDto szavazatLekerdezese(String szavazas, String kepviselo) {
        SzavazatEntity entity = szavazatRepository.findBySzavazas_IdAndKepviselo(szavazas, kepviselo).orElseThrow(() -> new SzavazasException(HttpStatus.NOT_FOUND, "Nincs ilyen szavazás, vagy az adott képviselő nem szavazott rajta."));
        return new SzavazatResponseDto(entity.getSzavazat().getKod());
    }

    @Override
    @Transactional(readOnly = true)
    public SzavazasEredmenyResponseDto eredmenyLekerdezese(String szavazas) {
        SzavazasEntity entity = repository.findById(szavazas).orElseThrow(() -> new SzavazasException(HttpStatus.NOT_FOUND, "Nincs szavazás a megadott azonosítóval."));
        return eredmenySzamitas(entity);
    }

    private SzavazasEredmenyResponseDto eredmenySzamitas(SzavazasEntity entity) {
        int igenekSzama = 0;
        int nemekSzama = 0;
        int tartozkodasokSzama = 0;

        for (SzavazatEntity szavazat : entity.getSzavazatok()) {
            switch (szavazat.getSzavazat()) {
                case IGEN -> igenekSzama++;
                case NEM -> nemekSzama++;
                case TARTOZKODAS -> tartozkodasokSzama++;
            }
        }

        int kepviselokSzama = switch (entity.getTipus()) {
            case JELENLET -> entity.getSzavazatok().size();
            case EGYSZERU -> {
                SzavazasEntity jelenletiSzavazas = repository.findFirstByTipusAndIdopontBeforeOrderByIdopontDesc(SzavazasTipus.JELENLET, entity.getIdopont()).orElseThrow(() -> new SzavazasException(HttpStatus.CONFLICT, "Az eredmény nem számítható ki: a szavazást nem előzte meg jelenléti szavazás."));
                yield jelenletiSzavazas.getSzavazatok().size();
            }
            case MINOSITETT -> OSSZES_KEPVISELO_SZAMA;
        };

        String eredmeny = entity.getTipus() == SzavazasTipus.JELENLET || igenekSzama > kepviselokSzama / 2 ? "F" : "U";
        return new SzavazasEredmenyResponseDto(eredmeny, kepviselokSzama, igenekSzama, nemekSzama, tartozkodasokSzama);
    }
}
