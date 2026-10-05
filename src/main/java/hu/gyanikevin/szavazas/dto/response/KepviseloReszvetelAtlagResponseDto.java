package hu.gyanikevin.szavazas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class KepviseloReszvetelAtlagResponseDto {

    private final BigDecimal atlag;
}
