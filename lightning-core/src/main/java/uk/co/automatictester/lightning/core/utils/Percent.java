package uk.co.automatictester.lightning.core.utils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Percent {

    private final int value;

    private Percent(int value) {
        if (isPercent(value)) {
            this.value = value;
        } else {
            String errorMessage = String.format("Incorrect value: %s. Should be integer in range 0-100", value);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static Percent from(int value) {
        return new Percent(value);
    }

    public int value() {
        return value;
    }

    private boolean isPercent(int percent) {
        return (percent >= 0) && (percent <= 100);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
