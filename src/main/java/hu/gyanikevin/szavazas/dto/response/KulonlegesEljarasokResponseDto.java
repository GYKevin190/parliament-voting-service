package hu.gyanikevin.szavazas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class KulonlegesEljarasokResponseDto {

    private final List<KulonlegesEljarasSzamResponseDto> szavazasok;
}
