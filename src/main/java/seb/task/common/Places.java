package seb.task.common;

import java.util.Arrays;
import java.util.Objects;

public enum Places {

    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4),
    FIFTH(5),
    SIXTH(6),
    SEVENTH(7),
    EIGHTH(8),
    NINTH(9),
    TENTH(10),
    ELEVENTH(11),
    TWELFTH(12),
    THIRTEENTH(13),
    FOURTEENTH(14),
    FIFTEENTH(15);

    private final Integer place;

    Places(Integer place) {
        this.place = place;
    }

    public Integer getPlace() {
        return place;
    }

    public static Places findByNumber(Integer place) {
        if (Objects.isNull(place)) { return null; }
        return Arrays.stream(Places.values())
                .filter(value -> value.getPlace().equals(place))
                .findFirst()
                .orElse(null);
    }

}
