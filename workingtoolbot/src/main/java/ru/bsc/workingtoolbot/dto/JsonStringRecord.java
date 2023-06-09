package ru.bsc.workingtoolbot.dto;

import org.apache.commons.lang3.StringUtils;

public record JsonStringRecord(String title, String required, String dataType, String regex) {
    public String bounds() {
        return StringUtils.substringBetween(dataType, "(", ")");
    }
}
