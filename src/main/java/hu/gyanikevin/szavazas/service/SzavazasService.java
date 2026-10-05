package hu.gyanikevin.szavazas.service;

import hu.gyanikevin.szavazas.dto.request.SzavazasRequestDto;
import hu.gyanikevin.szavazas.dto.response.SzavazasResponseDto;

public interface SzavazasService {

    SzavazasResponseDto szavazasMentese(SzavazasRequestDto dto);
}
