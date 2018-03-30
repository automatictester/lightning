package uk.co.automatictester.lightning.utils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uk.co.automatictester.lightning.exceptions.PercentException;
import uk.co.deliverymind.lightning.exceptions.PercentException;

public class Percent {

    private int value;

    public Percent(int value) {
        if (isPercent(value)) {
            this.value = value;
        } else {
            throw new PercentException(String.format("Incorrect value: %s. Should be integrer in range 0-100", value));
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    private boolean isPercent(int percent) {
        return (percent >= 0) && (percent <= 100);
    }

}
