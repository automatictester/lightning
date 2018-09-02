package uk.co.automatictester.lightning.core.enums;

import org.apache.commons.lang3.StringUtils;

public enum TestResult {
    PASS {
        @Override
        public String toString() {
            String lowerCaseName = this.name().toLowerCase();
            return StringUtils.capitalize(lowerCaseName);
        }
    },
    FAIL,
    ERROR
}
