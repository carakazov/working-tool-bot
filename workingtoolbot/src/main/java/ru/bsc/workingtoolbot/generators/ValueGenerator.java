package ru.bsc.workingtoolbot.generators;

import java.util.Objects;

import com.mifmif.common.regex.Generex;
import org.apache.commons.lang3.StringUtils;

public abstract class ValueGenerator {
    public String generate(String bounds, String regEx) {
        if(StringUtils.isNotBlank(regEx)) {
            Generex generex = new Generex(regEx);
            return generex.random();
        } else {
            if(Objects.nonNull(bounds)) {
                return generateWhenBounds(bounds);
            } else {
                return generateWithoutBounds();
            }
        }
    }

    protected abstract String generateWhenBounds(String bounds);

    protected abstract String generateWithoutBounds();

    public abstract String getType();
}
