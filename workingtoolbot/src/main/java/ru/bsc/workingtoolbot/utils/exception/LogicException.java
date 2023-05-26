package ru.bsc.workingtoolbot.utils.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogicException extends RuntimeException {
    private String message;
}
