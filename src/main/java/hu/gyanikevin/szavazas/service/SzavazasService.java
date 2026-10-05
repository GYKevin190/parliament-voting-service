package hu.gyanikevin.szavazas.service;

import hu.gyanikevin.szavazas.dto.request.SzavazasRequestDto;
import hu.gyanikevin.szavazas.dto.response.SzavazasResponseDto;
import hu.gyanikevin.szavazas.dto.response.SzavazasEredmenyResponseDto;
import hu.gyanikevin.szavazas.dto.response.SzavazatResponseDto;

public interface SzavazasService {

    SzavazasResponseDto szavazasMentese(SzavazasRequestDto dto);

    SzavazatResponseDto szavazatLekerdezese(String szavazas, String kepviselo);

    SzavazasEredmenyResponseDto eredmenyLekerdezese(String szavazas);
}
