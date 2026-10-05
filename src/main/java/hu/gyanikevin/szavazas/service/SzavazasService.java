package hu.gyanikevin.szavazas.service;

import hu.gyanikevin.szavazas.dto.request.SzavazasRequestDto;
import hu.gyanikevin.szavazas.dto.response.KepviseloReszvetelAtlagResponseDto;
import hu.gyanikevin.szavazas.dto.response.KulonlegesEljarasokResponseDto;
import hu.gyanikevin.szavazas.dto.response.NapiSzavazasokResponseDto;
import hu.gyanikevin.szavazas.dto.response.SzavazasResponseDto;
import hu.gyanikevin.szavazas.dto.response.SzavazasEredmenyResponseDto;
import hu.gyanikevin.szavazas.dto.response.SzavazatResponseDto;

import java.time.LocalDate;

public interface SzavazasService {

    SzavazasResponseDto szavazasMentese(SzavazasRequestDto dto);

    SzavazatResponseDto szavazatLekerdezese(String szavazas, String kepviselo);

    SzavazasEredmenyResponseDto eredmenyLekerdezese(String szavazas);

    NapiSzavazasokResponseDto napiSzavazasokLekerdezese(LocalDate nap);

    KepviseloReszvetelAtlagResponseDto kepviseloReszvetelAtlag(LocalDate kezdet, LocalDate veg);

    KulonlegesEljarasokResponseDto kulonlegesEljarasokSzama(LocalDate kezdet, LocalDate veg);
}
