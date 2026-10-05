package hu.gyanikevin.szavazas.dto.request;

import hu.gyanikevin.szavazas.model.enums.SzavazatTipus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SzavazatRequestDto {

    @NotNull(message = "A képviselő megadása kötelező.")
    private String kepviselo;

    @NotNull(message = "A szavazat megadása kötelező.")
    private SzavazatTipus szavazat;
}
