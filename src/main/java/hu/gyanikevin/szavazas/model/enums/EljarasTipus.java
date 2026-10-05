package hu.gyanikevin.szavazas.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EljarasTipus {

    NORMAL("n"),
    SURGOSSEGI("s"),
    KIVETELES("k"),
    SZABALYZATTOL_ELTERO("e");

    private final String kod;

    @JsonValue
    public String getKod() {
        return kod;
    }

    @JsonCreator
    public static EljarasTipus fromKod(String kod) {
        for (EljarasTipus tipus : values()) {
            if (tipus.kod.equals(kod)) {
                return tipus;
            }
        }
        throw new IllegalArgumentException("Érvénytelen kód: " + kod);
    }
}
