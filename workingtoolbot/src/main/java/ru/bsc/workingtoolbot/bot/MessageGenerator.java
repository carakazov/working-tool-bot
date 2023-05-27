package ru.bsc.workingtoolbot.bot;

import org.springframework.stereotype.Component;

@Component
public class MessageGenerator {
    public String generateStartMessage() {
        return "start";
    }

    public String generateCancelMessage() {
        return "Команда отменена";
    }

    public String generateHelpMessage() {
        return "Описание основных команд:\n" +
            "\n" +
            "/cancel - Отмена текущей команды\n" +
            "\n" +
            "/testdata_create - Создание шаблона для тестовых данных:\n" +
            "Получить шаблон можно сообщением или файлом. \n" +
            "Строка для списка полей должна быть в формате: имя_поля [обязательность(M/O)] тип_данных([размерность]) [regex]\n" +
            "Шаблоны можно переиспользовать - для этого название ранее сохраненного шаблона необходимо указать в типе данных поля\n" +
            "Доступные типы данных: string(x), int(x), decimal(y, z), boolean\n" +
            "x - кол-во символов\n" +
            "y - кол-во символов целой части\n" +
            "z - кол-во символов дробной части" +
            "/testdata_list - Получить шаблон из созданных ранее\n" +
            "\n" +
            "/testclasses_generate - Создание тестовых классов с константами и фабричными методами:\n" +
            "Необходимо загрузить все классы dto и моделей" + "\n" + "\n" +
            "/remove_all - Очистить все шаблоны\n" + "\n" +
            "/remove - Удалить конкретный шаблон из списка";
    }
}
