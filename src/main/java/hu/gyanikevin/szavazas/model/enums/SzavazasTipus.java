package hu.gyanikevin.szavazas.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SzavazasTipus {

    JELENLET("j"),
    EGYSZERU("e"),
    MINOSITETT("m");

    private final String kod;

    @JsonValue
    public String getKod() {
        return kod;
    }

    @JsonCreator
    public static SzavazasTipus fromKod(String kod) {
        for (SzavazasTipus tipus : values()) {
            if (tipus.kod.equals(kod)) {
                return tipus;
            }
        }
        throw new IllegalArgumentException("Érvénytelen kód: " + kod);
    }
}
