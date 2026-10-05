package hu.gyanikevin.szavazas.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import hu.gyanikevin.szavazas.model.enums.EljarasTipus;
import hu.gyanikevin.szavazas.model.enums.SzavazasTipus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
public class NapiSzavazasResponseDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final Instant idopont;
    private final String targy;
    private final SzavazasTipus tipus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final EljarasTipus eljaras;
    private final String elnok;
    private final String eredmeny;
    private final int kepviselokSzama;
    private final List<SzavazatAdatResponseDto> szavazatok;
}
