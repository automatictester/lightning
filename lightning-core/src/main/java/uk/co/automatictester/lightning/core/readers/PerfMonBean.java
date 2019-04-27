package uk.co.automatictester.lightning.core.readers;

import com.univocity.parsers.annotations.Parsed;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PerfMonBean implements CsvBean {

    public PerfMonBean() {
    }

    public PerfMonBean(String timeStamp, String value, String hostAndMetric) {
        this.timeStamp = Long.parseLong(timeStamp);
        this.value = Integer.parseInt(value);
        this.hostAndMetric = hostAndMetric;
    }

    @Parsed(index = 0)
    private long timeStamp;

    @Parsed(index = 1)
    private int value;

    @Parsed(index = 2)
    private String hostAndMetric;

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getValue() {
        return value;
    }

    public String getHostAndMetric() {
        return hostAndMetric;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
