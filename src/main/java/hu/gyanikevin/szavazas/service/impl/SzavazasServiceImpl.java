package hu.gyanikevin.szavazas.service.impl;

import hu.gyanikevin.szavazas.dto.request.SzavazasRequestDto;
import hu.gyanikevin.szavazas.dto.request.SzavazatRequestDto;
import hu.gyanikevin.szavazas.dto.response.KepviseloReszvetelAtlagResponseDto;
import hu.gyanikevin.szavazas.dto.response.KulonlegesEljarasokResponseDto;
import hu.gyanikevin.szavazas.dto.response.KulonlegesEljarasSzamResponseDto;
import hu.gyanikevin.szavazas.dto.response.NapiSzavazasokResponseDto;
import hu.gyanikevin.szavazas.dto.response.NapiSzavazasResponseDto;
import hu.gyanikevin.szavazas.dto.response.SzavazatAdatResponseDto;
import hu.gyanikevin.szavazas.dto.response.SzavazasResponseDto;
import hu.gyanikevin.szavazas.dto.response.SzavazasEredmenyResponseDto;
import hu.gyanikevin.szavazas.dto.response.SzavazatResponseDto;
import hu.gyanikevin.szavazas.exception.SzavazasException;
import hu.gyanikevin.szavazas.model.SzavazasEntity;
import hu.gyanikevin.szavazas.model.SzavazatEntity;
import hu.gyanikevin.szavazas.model.enums.EljarasTipus;
import hu.gyanikevin.szavazas.model.enums.SzavazasTipus;
import hu.gyanikevin.szavazas.repository.SzavazasRepository;
import hu.gyanikevin.szavazas.repository.SzavazatRepository;
import hu.gyanikevin.szavazas.service.SzavazasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SzavazasServiceImpl implements SzavazasService {

    private static final int OSSZES_KEPVISELO_SZAMA = 200;
    private static final List<EljarasTipus> KULONLEGES_ELJARASOK = List.of(EljarasTipus.SURGOSSEGI, EljarasTipus.KIVETELES, EljarasTipus.SZABALYZATTOL_ELTERO);

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

    @Override
    @Transactional(readOnly = true)
    public NapiSzavazasokResponseDto napiSzavazasokLekerdezese(LocalDate nap) {
        Instant kezdet = nap.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant veg = nap.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        List<NapiSzavazasResponseDto> szavazasok = new ArrayList<>();

        for (SzavazasEntity entity : repository.findByIdopontGreaterThanEqualAndIdopontLessThanOrderByIdopontAsc(kezdet, veg)) {
            SzavazasEredmenyResponseDto eredmeny = eredmenySzamitas(entity);
            List<SzavazatAdatResponseDto> szavazatok = entity.getSzavazatok().stream().map(szavazat -> new SzavazatAdatResponseDto(szavazat.getKepviselo(), szavazat.getSzavazat())).toList();
            szavazasok.add(new NapiSzavazasResponseDto(entity.getIdopont(), entity.getTargy(), entity.getTipus(), entity.getEljaras(), entity.getElnok(), eredmeny.getEredmeny(), eredmeny.getKepviselokSzama(), szavazatok));
        }

        return new NapiSzavazasokResponseDto(szavazasok);
    }

    @Override
    @Transactional(readOnly = true)
    public KepviseloReszvetelAtlagResponseDto kepviseloReszvetelAtlag(LocalDate kezdet, LocalDate veg) {
        idoszakEllenorzes(kezdet, veg);
        Instant idopontKezdet = kezdet.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant idopontVeg = veg.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        long reszvetelekSzama = szavazatRepository.countReszvetelek(SzavazasTipus.JELENLET, idopontKezdet, idopontVeg);
        BigDecimal atlag = BigDecimal.valueOf(reszvetelekSzama).divide(BigDecimal.valueOf(OSSZES_KEPVISELO_SZAMA), 2, RoundingMode.HALF_UP);
        return new KepviseloReszvetelAtlagResponseDto(atlag);
    }

    @Override
    @Transactional(readOnly = true)
    public KulonlegesEljarasokResponseDto kulonlegesEljarasokSzama(LocalDate kezdet, LocalDate veg) {
        idoszakEllenorzes(kezdet, veg);
        Instant idopontKezdet = kezdet.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant idopontVeg = veg.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        Map<EljarasTipus, Long> elfogadottak = new EnumMap<>(EljarasTipus.class);
        Map<EljarasTipus, Long> elutasitottak = new EnumMap<>(EljarasTipus.class);

        for (SzavazasEntity entity : repository.findByEljarasInAndIdopontGreaterThanEqualAndIdopontLessThanOrderByIdopontAsc(KULONLEGES_ELJARASOK, idopontKezdet, idopontVeg)) {
            String eredmeny = eredmenySzamitas(entity).getEredmeny();
            if ("F".equals(eredmeny)) {
                elfogadottak.merge(entity.getEljaras(), 1L, Long::sum);
            } else {
                elutasitottak.merge(entity.getEljaras(), 1L, Long::sum);
            }
        }

        List<KulonlegesEljarasSzamResponseDto> szavazasok = new ArrayList<>();
        long osszesElfogadott = 0;
        long osszesElutasitott = 0;

        for (EljarasTipus eljaras : KULONLEGES_ELJARASOK) {
            long elfogadott = elfogadottak.getOrDefault(eljaras, 0L);
            long elutasitott = elutasitottak.getOrDefault(eljaras, 0L);
            szavazasok.add(new KulonlegesEljarasSzamResponseDto(eljaras.getKod(), "F", elfogadott));
            szavazasok.add(new KulonlegesEljarasSzamResponseDto(eljaras.getKod(), "U", elutasitott));
            osszesElfogadott += elfogadott;
            osszesElutasitott += elutasitott;
        }

        szavazasok.add(new KulonlegesEljarasSzamResponseDto("összes", "F", osszesElfogadott));
        szavazasok.add(new KulonlegesEljarasSzamResponseDto("összes", "U", osszesElutasitott));
        szavazasok.add(new KulonlegesEljarasSzamResponseDto("összes", "összes", osszesElfogadott + osszesElutasitott));
        return new KulonlegesEljarasokResponseDto(szavazasok);
    }

    private void idoszakEllenorzes(LocalDate kezdet, LocalDate veg) {
        if (veg.isBefore(kezdet)) {
            throw new SzavazasException(HttpStatus.BAD_REQUEST, "Az időszak vége nem lehet korábbi a kezdeténél.");
        }
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
