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


    public InlineKeyboardMarkup getChooseTesDataTypeKeyboard() {
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Сообщением");
        button1.setCallbackData(CallbackType.TMP_MESSAGE);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Файлом");
        button2.setCallbackData(CallbackType.TMP_FILE);

        keyboardRow.add(button1);
        keyboardRow.add(button2);
        keyboard.setKeyboard(Arrays.asList(keyboardRow));

        return keyboard;
    }
}
