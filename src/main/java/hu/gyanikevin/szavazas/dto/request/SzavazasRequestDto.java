package hu.gyanikevin.szavazas.dto.request;

import hu.gyanikevin.szavazas.model.enums.EljarasTipus;
import hu.gyanikevin.szavazas.model.enums.SzavazasTipus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class SzavazasRequestDto {

    @NotNull(message = "Az időpont megadása kötelező.")
    private OffsetDateTime idopont;

    @NotNull(message = "A tárgy megadása kötelező.")
    private String targy;

    @NotNull(message = "A szavazás típusának megadása kötelező.")
    private SzavazasTipus tipus;

    private EljarasTipus eljaras;

    @NotNull(message = "Az elnök megadása kötelező.")
    private String elnok;

    @NotNull(message = "A szavazatok megadása kötelező.")
    private List<@NotNull(message = "A szavazat nem lehet null.") @Valid SzavazatRequestDto> szavazatok;
}
