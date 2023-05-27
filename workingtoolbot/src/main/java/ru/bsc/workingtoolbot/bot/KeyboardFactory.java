package ru.bsc.workingtoolbot.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Service
public class KeyboardFactory {

    private final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();


    public InlineKeyboardMarkup getChooseTestDataTypeKeyboard(Boolean list) {
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Сообщением");
        button1.setCallbackData(list ? CallbackType.TMP_LIST_MESSAGE : CallbackType.TMP_MESSAGE);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Файлом");
        button2.setCallbackData(list ? CallbackType.TMP_LIST_FILE : CallbackType.TMP_FILE);

        keyboardRow.add(button1);
        keyboardRow.add(button2);
        keyboard.setKeyboard(Arrays.asList(keyboardRow));

        return keyboard;
    }

    public InlineKeyboardMarkup getChooseUploadFinished() {
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Да");
        button1.setCallbackData(CallbackType.TC_UPLOAD_FINISHED);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Нет");
        button2.setCallbackData(CallbackType.TC_UPLOAD_NOT_FINISHED);

        keyboardRow.add(button1);
        keyboardRow.add(button2);
        keyboard.setKeyboard(Arrays.asList(keyboardRow));

        return keyboard;
    }
}
