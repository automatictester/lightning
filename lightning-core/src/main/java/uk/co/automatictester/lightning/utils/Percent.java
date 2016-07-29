package uk.co.automatictester.lightning.utils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import uk.co.automatictester.lightning.exceptions.PercentException;

public class Percent {

    private int percent;

    public Percent(int number) {
        if (isPercent(number)) {
            this.percent = number;
        } else {
            throw new PercentException(String.format("Incorrect value: %s. Should be integrer in range 0-100", number));
        }
    }

    public int getPercent() {
        return percent;
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    private boolean isPercent(int percent) {
        return ((percent >= 0) && (percent <= 100));
    }

}
