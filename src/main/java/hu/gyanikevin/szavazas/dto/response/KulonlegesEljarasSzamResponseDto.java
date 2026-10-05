package hu.gyanikevin.szavazas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KulonlegesEljarasSzamResponseDto {

    private final String eljaras;
    private final String eredmeny;
    private final long szam;
}
