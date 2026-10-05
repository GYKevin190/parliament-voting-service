package hu.gyanikevin.szavazas.dto.response;

import hu.gyanikevin.szavazas.model.enums.SzavazatTipus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SzavazatAdatResponseDto {

    private final String kepviselo;
    private final SzavazatTipus szavazat;
}
