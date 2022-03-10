package ru.liga.input;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.exceptions.BaseException;
import ru.liga.executors.CommandExecutor;
import ru.liga.executors.DataCommandExecutor;
import ru.liga.executors.ExecutorController;
import ru.liga.output.CommandResult;
import ru.liga.repository.CurrencyRepository;

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
                    .getUserCommand();
            CommandExecutor executor = new ExecutorController(repository).getExecutor(userCommand);
            CommandResult result = executor.execute();
            setAnswer(userInput.getChatId(), userInput.getFrom().getUserName(), result);
        } catch (BaseException e) {
            setAnswer(userInput.getChatId(), userInput.getFrom().getUserName(), e.getMessage());
        }
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    private void setAnswer(Long chatId, String userName, String error) {
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId.toString());
        answer.setText(error);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error: {}; from: {}; result: {}", e.getMessage(), userName, error);
        }
    }

    private void setAnswer(Long chatId, String userName, CommandResult result) {
        try {
            if (result.isText()) {
                SendMessage answer = new SendMessage();
                answer.setChatId(chatId.toString());
                answer.setText(result.getTextResult());
                execute(answer);
            } else {
                SendPhoto answer = new SendPhoto();
                answer.setChatId(chatId.toString());
                answer.setPhoto(new InputFile(result.getGraphResult()));
                execute(answer);
            }
        } catch (TelegramApiException e) {
            log.error("Error: {}; from: {}; result: {}", e.getMessage(), userName, result);
        }
    }

}
