package hu.gyanikevin.szavazas.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SzavazatTipus {

    IGEN("i"),
    NEM("n"),
    TARTOZKODAS("t");

    private final String kod;

    @JsonValue
    public String getKod() {
        return kod;
    }

    @JsonCreator
    public static SzavazatTipus fromKod(String kod) {
        for (SzavazatTipus tipus : values()) {
            if (tipus.kod.equals(kod)) {
                return tipus;
            }
        }
        throw new IllegalArgumentException("Érvénytelen kód: " + kod);
    }
}
