package ru.liga.input;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.currencies.CurrencyRate;
import ru.liga.exceptions.*;
import ru.liga.repository.CurrencyRepository;

import java.io.File;
import java.util.List;

@Slf4j
public class Bot extends TelegramLongPollingCommandBot {

    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private final CurrencyRepository repository;

    public Bot(String botName, String botToken, CurrencyRepository repository) {
        super();
        this.repository = repository;
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Message userInput = update.getMessage();
        log.info("Input: {}; from user: {}", userInput.getText(), userInput.getFrom().getUserName());
        try {
            UserCommand userCommand = new UserCommandParser(userInput.getText())
                    .getUserCommand()
                    .setRepository(repository);
            userCommand.execute();
            if (userCommand.isGraph()) {
                File graph = userCommand.getGraphFile();
                setAnswer(userInput.getChatId(), userInput.getFrom().getUserName(), graph);
            } else {
                List<CurrencyRate> answer = userCommand.getForecast();
                setAnswer(userInput.getChatId(), userInput.getFrom().getUserName(), answer);
            }
        } catch (BaseException e) {
            setAnswer(userInput.getChatId(), userInput.getFrom().getUserName(), e.getMessage());
        }
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    /**
     * Отправка ответа
     * @param chatId chat Id :)
     * @param userName userName
     * @param data List of data to display
     */
    private void setAnswer(Long chatId, String userName, List<CurrencyRate> data) {
        SendMessage answer = new SendMessage();
        StringBuilder stringBuilder = new StringBuilder();
        data.forEach(currencyRate -> stringBuilder.append(currencyRate).append("\n"));
        answer.setText(stringBuilder.toString());
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            //логируем сбой Telegram Bot API, используя userName
        }
    }

    private void setAnswer(Long chatId, String userName, String error) {
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId.toString());
        answer.setText(error);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            //логируем сбой Telegram Bot API, используя userName
        }
    }

    private void setAnswer(Long chatId, String userName, File graph) {
        SendPhoto answer = new SendPhoto();
        answer.setChatId(chatId.toString());
        answer.setPhoto(new InputFile(graph));
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
