package uk.co.automatictester.lightning.core.readers;

import com.univocity.parsers.annotations.BooleanString;
import com.univocity.parsers.annotations.Headers;
import com.univocity.parsers.annotations.Parsed;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class JmeterBean implements CsvBean {

    public JmeterBean() {
    }

    public JmeterBean(String label, String elapsed, String success) {
        this.label = label;
        this.elapsed = Integer.parseInt(elapsed);
        this.success = Boolean.parseBoolean(success);
    }

    public JmeterBean(String label, String elapsed, String success, String timeStamp) {
        this(label, elapsed, success);
        this.timeStamp = Long.parseLong(timeStamp);
    }

    @Parsed
    private long timeStamp;

    @Parsed
    private int elapsed;

    @Parsed
    private String label;

    @Parsed
    @BooleanString(trueStrings = "true", falseStrings = "false")
    private boolean success;

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getElapsed() {
        return elapsed;
    }

    public String getLabel() {
        return label;
    }

    public boolean isSuccess() {
        return success;
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
