package hu.gyanikevin.szavazas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NapiSzavazasokResponseDto {

    private final List<NapiSzavazasResponseDto> szavazasok;
}
