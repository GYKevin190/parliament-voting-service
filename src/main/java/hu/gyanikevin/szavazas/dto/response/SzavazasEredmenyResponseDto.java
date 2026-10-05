package hu.gyanikevin.szavazas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SzavazasEredmenyResponseDto {

    private final String eredmeny;
    private final int kepviselokSzama;
    private final int igenekSzama;
    private final int nemekSzama;
    private final int tartozkodasokSzama;
}
