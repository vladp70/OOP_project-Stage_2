package enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AgeGroup {

    @JsonProperty("Baby")
    BABY("Baby"),

    @JsonProperty("Kid")
    KID("Kid"),

    @JsonProperty("Teen")
    TEEN("Teen"),

    @JsonProperty("Young Adult")
    YOUNG_ADULT("Young Adult"),

    @JsonProperty("Unknown")
    UNKNOWN("Unknown");

    private String value;

    AgeGroup(final String value) {
        this.value = value;
    }
}
